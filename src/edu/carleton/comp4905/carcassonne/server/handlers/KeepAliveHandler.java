package edu.carleton.comp4905.carcassonne.server.handlers;

import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.server.Server;
import edu.carleton.comp4905.carcassonne.server.ServerController;

public class KeepAliveHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Server server = (Server)connection.getService();
		ServerController controller = server.getController();
		
		controller.addMessageEntry(MessageType.INFO, LocalMessages.getString("KeepAliveReceived") + event.getPlayerName());
	}
}
