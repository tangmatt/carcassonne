package edu.carleton.comp4905.carcassonne.server.handlers;

import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.Player;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class ScoreUpdateRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		ConcurrentMap<Address, Connection> connections = server.getConnections();
		
		String target = (String)event.getProperty("target");
		int points = (int)event.getProperty("points");
		Player.Status[] statuses = controller.getStatuses(connections);
		String[] names = controller.getPlayerNames();
		
		controller.addPlayerScore(target, points);
		
		// send reply back to connected clients
		Event reply = new Event(EventType.SCORE_UPDATE_REPLY, event.getPlayerName());
		reply.addProperty("names", names);
		reply.addProperty("statuses", statuses);
		reply.addProperty("target", target);
		reply.addProperty("points", controller.getPlayerScore(target));
		connection.broadcastEvent(reply, connections);
	}
}
