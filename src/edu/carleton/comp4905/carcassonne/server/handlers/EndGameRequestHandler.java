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

public class EndGameRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();

		controller.addMessageEntry(MessageType.INFO, "There are no more tiles in the deck.");
		server.getReactor().removeHandler(EventType.QUIT_REQUEST);

		// send reply back to connected client
		Event reply = new Event(EventType.END_GAME_REPLY, event.getPlayerName());
		reply.addProperty("message", "There are no more tiles in the deck.");
		connection.broadcastEvent(reply, connections);
		
		controller.closeApplication();
	}
}
