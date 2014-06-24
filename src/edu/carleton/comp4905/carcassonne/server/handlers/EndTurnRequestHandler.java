package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
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
		
		if(server.getMode() == Mode.SYNC)
			server.setNextPlayer();
		controller.updatePlayerHand(event.getPlayerName(), false);
		
		boolean success = !(controller.allPlayersHaveNoTile() && controller.getDeck().isEmpty());
		
		// send reply back to connected client
		Event reply = new Event(EventType.END_TURN_REPLY, event.getPlayerName());
		reply.addProperty("success", success);
		reply.addProperty("target", server.getCurrentPlayer());
		connection.broadcastEvent(reply, connections);
	}
}
