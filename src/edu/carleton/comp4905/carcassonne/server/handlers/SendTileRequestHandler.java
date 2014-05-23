package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.server.Server;

public class SendTileRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		String tile = (String)event.getProperty("tile");
		int rotation = (int)event.getProperty("rotation");
		int row = (int)event.getProperty("row");
		int column = (int)event.getProperty("column");
		
		// send reply back to connected clients
		Event reply = new Event(EventType.SEND_TILE_REPLY, event.getPlayerName());
		reply.addProperty("tile", tile + rotation); // append degrees to name to get the rotated tile (tile name convention from TileManager)
		reply.addProperty("row", row);
		reply.addProperty("column", column);
		connection.broadcastEvent(reply, connections);
	}
}
