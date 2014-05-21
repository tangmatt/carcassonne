package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.Iterator;
import java.util.Map;
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
		
		// remove related connection
		Iterator<Map.Entry<Address, Connection>> it = connections.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Address, Connection> pairs = (Map.Entry<Address, Connection>)it.next();
			Address temp = (Address)pairs.getKey();
			if(temp.equals(new Address(address, port)))
				it.remove();
		}
		
		controller.updatePlayer(event.getPlayerName(), address, portAsString, Status.DISCONNECTED);
		controller.addMessageEntry(MessageType.INFO, "Player (" + event.getPlayerName() + ") has quit");
		
		boolean[] statuses = new boolean[controller.getPlayers().size()];
		for(int i=0; i<statuses.length; ++i)
			statuses[i] = controller.getPlayers().get(i).isConnected();
		
		// send reply back to connected clients
		Event reply = new Event(EventType.QUIT_REPLY, event.getPlayerName());
		reply.addProperty("statuses", statuses);
		connection.broadcastEvent(reply, connections);
	}
}
