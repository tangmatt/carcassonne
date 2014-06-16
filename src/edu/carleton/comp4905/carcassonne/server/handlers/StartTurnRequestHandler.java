package edu.carleton.comp4905.carcassonne.server.handlers;

import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class StartTurnRequestHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		
		// send reply back to connected client
		Event reply = new Event(EventType.START_TURN_REPLY, event.getPlayerName());
		reply.addProperty("tile", controller.getDeck().draw());
		connection.sendEvent(reply);
	}
}
