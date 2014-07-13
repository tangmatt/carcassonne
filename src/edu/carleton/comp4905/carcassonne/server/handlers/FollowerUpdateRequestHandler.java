package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.server.Server;

public class FollowerUpdateRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		String target = (String)event.getProperty("target");
		int followers = (int)event.getProperty("followers");
		
		// send reply back to connected clients
		Event reply = new Event(EventType.FOLLOWER_UPDATE_REPLY, event.getPlayerName());
		reply.addProperty("target", target);
		reply.addProperty("followers", followers);
		connection.broadcastEvent(reply, connections);
	}
}