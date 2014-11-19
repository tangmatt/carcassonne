package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.TileManager;
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
		TileManager tileManager = TileManager.getInstance();

		String checksum = (String)event.getProperty("checksum");
		
		// send reply back to sending client if game is in progress or player name already taken
		if(server.isGameInProgress()) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", LocalMessages.getString("GameAlreadyStarted"));
			controller.sendEvent(reply, connection);
			return;
		} else if(controller.playerExists(event.getPlayerName())) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", LocalMessages.getString("PlayerAlreadyExists"));
			controller.sendEvent(reply, connection);
			return;
		} else if(connections.size() >= 5) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", LocalMessages.getString("PlayerLimitReached"));
			controller.sendEvent(reply, connection);
			return;
		} else if(!checksum.equals(tileManager.checksum())) {
			Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
			reply.addProperty("numOfPlayers", connections.size());
			reply.addProperty("success", false);
			reply.addProperty("message", LocalMessages.getString("ModifiedTileConfig"));
			controller.sendEvent(reply, connection);
			return;
		}
		
		server.addPlayer(event.getPlayerName());
		connections.put(new Address(address, port), connection);
		controller.connectPlayer(event.getPlayerName(), address, portAsString);
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has joined the lobby");

		// send reply back to connected clients
		Event reply = new Event(EventType.JOIN_REPLY, event.getPlayerName());
		reply.addProperty("numOfPlayers", connections.size());
		reply.addProperty("success", true);
		controller.broadcastEvent(reply, connection, connections);
	}
}
