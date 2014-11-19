package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Player;
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
		
		server.removePlayer(event.getPlayerName());
		if(!controller.updatePlayer(event.getPlayerName(), address, portAsString, Status.DISCONNECTED))
			return;
		controller.addMessageEntry(MessageType.INFO, "Player '" + event.getPlayerName() + "' has quit");
		
		if(connections.isEmpty())
			controller.handleGameFinish();
		
		String[] names = controller.getPlayerNames();
		String title = null, message = null;
		Player.Status[] statuses = controller.getStatuses(connections);
		boolean gameOver = (controller.getNumberOfPlayersConnected() == 1) && server.isGameInProgress();

		if(gameOver) {
			Player player = controller.getRemainingPlayer();
			title = LocalMessages.getString("LastPlayerRemainsTitle");
			message = player.getName() + " is the winner!";
		} else if(!server.isGameInProgress()) {
			controller.removePlayer(event.getPlayerName(), address, portAsString, Status.DISCONNECTED);
			server.removePlayer(event.getPlayerName());
		}

		// send reply back to connected clients
		Event reply = new Event(EventType.QUIT_REPLY, event.getPlayerName());
		reply.addProperty("numOfPlayers", connections.size()-1);
		reply.addProperty("statuses", statuses);
		reply.addProperty("names", names);
		reply.addProperty("finished", gameOver);
		reply.addProperty("message", message);
		reply.addProperty("gameInProgress", server.isGameInProgress());
		reply.addProperty("messageTitle", title);
		reply.addProperty("message", message);
		controller.broadcastEvent(reply, connection, connections);
		
		controller.removeConnection(connections, address, port);
	}
}
