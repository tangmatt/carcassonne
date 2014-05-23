package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Player.Status;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class QuitRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		String address = (String)event.getProperty("address");
		Integer port = (Integer)event.getProperty("port");
		String portAsString = port.toString();
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		controller.removeConnection(connections, address, port);
		controller.updatePlayer(event.getPlayerName(), address, portAsString, Status.DISCONNECTED);
		controller.addMessageEntry(MessageType.INFO, "Player (" + event.getPlayerName() + ") has quit");
		
		if(connections.isEmpty())
			controller.handleGameFinish();
		
		boolean[] statuses = controller.getStatuses(connections);
		String[] names = controller.getPlayerNames();
		
		// send reply back to connected clients
		Event reply = new Event(EventType.QUIT_REPLY, event.getPlayerName());
		reply.addProperty("statuses", statuses);
		reply.addProperty("playerNames", names);
		connection.broadcastEvent(reply, connections);
	}
}
