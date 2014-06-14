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

public class JoinRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		String address = (String)event.getProperty("address");
		Integer port = (Integer)event.getProperty("port");
		String portAsString = port.toString();
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();

		// send reply back to sending client if game is in progress or player name already taken
		if(server.isGameInProgress()) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", "Game has already started.");
			connection.sendEvent(reply);
			return;
		} else if(controller.playerExists(event.getPlayerName())) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", "Player name already exists.");
			connection.sendEvent(reply);
			return;
		} else if(connections.size() >= 5) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", "Lobby has reached the player limit.");
			connection.sendEvent(reply);
			return;
		}
		
		connections.put(new Address(address, port), connection);
		controller.connectPlayer(event.getPlayerName(), address, portAsString);
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has joined the lobby");

		// send reply back to connected clients
		Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
		reply.addProperty("numOfPlayers", connections.size());
		reply.addProperty("success", true);
		connection.broadcastEvent(reply, connections);
	}
}
