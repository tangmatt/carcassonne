package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class SendTileRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		String tile = (String)event.getProperty("tile");
		int rotation = (int)event.getProperty("rotation");
		int row = (int)event.getProperty("row");
		int column = (int)event.getProperty("column");
		int meeple = (int)event.getProperty("meeple");
		Position position = (Position)event.getProperty("position");
		boolean shield = (boolean)event.getProperty("shield");
		
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has placed a tile at row=" + row + ", column=" + column + ", name="+tile + ", rotation=" + rotation + ", shield=" + shield);
		
		// send reply back to connected clients
		Event reply = new Event(EventType.SEND_TILE_REPLY, event.getPlayerName());
		reply.addProperty("tile", tile);
		reply.addProperty("row", row);
		reply.addProperty("column", column);
		reply.addProperty("meeple", meeple);
		reply.addProperty("position", position);
		reply.addProperty("shield", shield);
		connection.broadcastEvent(reply, connections);
	}
}
