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

public class StartRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		server.gameInProgress(true);
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has started the game (with " + connections.size() + " players)");
		Boolean[] statuses = controller.getStatuses(connections);
		String[] names = controller.getPlayerNames();
		//String[] ids = controller.getPlayerIndices();
		
		// send reply back to connected clients
		Event reply = new Event(EventType.START_REPLY, event.getPlayerName());
		reply.addProperty("statuses", statuses);
		reply.addProperty("names", names);
		//reply.addProperty("ids", ids);
		connection.broadcastEvent(reply, connections);
	}
}
