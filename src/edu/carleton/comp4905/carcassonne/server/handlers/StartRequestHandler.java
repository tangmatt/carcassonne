package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Player;
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
		Player.Status[] statuses = controller.getStatuses(connections);
		String[] names = controller.getPlayerNames();
		Random random = new Random();
		int rows = Server.INITIAL_ROWS;
		int columns = Server.INITIAL_COLS;
		int row = random.nextInt(rows);
		int column = random.nextInt(columns);
		int points = controller.getPlayerScore(event.getPlayerName());
		
		// send reply back to connected clients
		Event reply = new Event(EventType.START_REPLY, event.getPlayerName());
		reply.addProperty("names", names);
		reply.addProperty("statuses", statuses);
		reply.addProperty("target", server.getCurrentPlayer());
		reply.addProperty("mode", server.getMode());
		reply.addProperty("rows", rows);
		reply.addProperty("columns", columns);
		reply.addProperty("row", row);
		reply.addProperty("column", column);
		reply.addProperty("points", points);
		connection.broadcastEvent(reply, connections);
	}
}
