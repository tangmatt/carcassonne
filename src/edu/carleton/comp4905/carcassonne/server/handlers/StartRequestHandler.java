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
		String address = (String)event.getProperty("address");
		Integer port = (Integer)event.getProperty("port");
		String portAsString = port.toString();
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has started the game (with " + connections.size() + " players)");
		boolean[] statuses = new boolean[connections.size()];
		for(int i=0; i<statuses.length; ++i) {
			statuses[i] = controller.getPlayers().get(i).isConnected();
		}
		
		// send reply back to connected clients
		Event reply = new Event(EventType.START_REPLY, event.getPlayerName());
		reply.addProperty("statuses", statuses);
		connection.broadcastEvent(reply, connections);
	}
}
