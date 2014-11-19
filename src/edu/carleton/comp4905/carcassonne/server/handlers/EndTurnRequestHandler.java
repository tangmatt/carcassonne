package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class EndTurnRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();

		String title = null, message = null;
		boolean isQuitting = (boolean)event.getProperty("quitting");
		String prevPlayer = new String(server.getCurrentPlayer());
		
		if(server.getMode() == Mode.SYNC) {
			if(isQuitting)
				server.removePlayer(event.getPlayerName());
			if(!prevPlayer.equalsIgnoreCase(event.getPlayerName())) {
				Event reply = new Event(EventType.END_TURN_REPLY, event.getPlayerName());
				reply.addProperty("success", !(controller.allPlayersHaveNoTile() && controller.getDeck().isEmpty()));
				reply.addProperty("messageTitle", title);
				reply.addProperty("message", message);
				reply.addProperty("finished", server.getCurrentPlayer().equalsIgnoreCase(event.getPlayerName()));
				reply.addProperty("quitting", isQuitting);
				controller.broadcastEvent(reply, connection, connections);
				return;
			}
			server.setNextPlayer();
		}

		controller.updatePlayerHand(event.getPlayerName(), false);
		boolean success = !(controller.allPlayersHaveNoTile() && controller.getDeck().isEmpty());
		if(!success) {
			title = LocalMessages.getString("EmptyDeckTitle");
			message = controller.getWinnerMessage();
		}

		// send reply back to connected client
		Event reply = new Event(EventType.END_TURN_REPLY, event.getPlayerName());
		reply.addProperty("success", success);
		reply.addProperty("target", server.getCurrentPlayer());
		reply.addProperty("messageTitle", title);
		reply.addProperty("message", message);
		reply.addProperty("finished", server.getCurrentPlayer().equalsIgnoreCase(event.getPlayerName()));
		reply.addProperty("quitting", isQuitting);
		controller.broadcastEvent(reply, connection, connections);
	}
}
