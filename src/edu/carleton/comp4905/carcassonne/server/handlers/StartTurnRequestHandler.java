package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class StartTurnRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		Boolean[] statuses = controller.getStatuses(connections);
		String tile = null;
		
		if(!controller.getDeck().isEmpty()) {
			tile = controller.getDeck().draw();
			controller.updatePlayerHand(event.getPlayerName(), true);
		} else {
			controller.updatePlayerHand(event.getPlayerName(), false);
		}
		
		controller.addMessageEntry(MessageType.INFO, "Player " + event.getPlayerName() + " drew tile: " + tile
				+ " (" + controller.getDeck().size() + " tiles left in deck)");
		
		// send reply back to connected client
		Event reply = new Event(EventType.START_TURN_REPLY, event.getPlayerName());
		reply.addProperty("tile", tile);
		reply.addProperty("target", server.getCurrentPlayer());
		reply.addProperty("targetIndex", server.getTurn());
		reply.addProperty("statuses", statuses);
		connection.broadcastEvent(reply, connections);
	}
}
