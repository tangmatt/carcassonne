package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;

public class EndTurnReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String player = event.getPlayerName();
		
		boolean success = (boolean)event.getProperty("success");
		String target = (String)event.getProperty("target");
		String title = (String)event.getProperty("messageTitle");
		String message = (String)event.getProperty("message");
		boolean finished = (boolean)event.getProperty("finished");
		boolean isQuitting = (boolean)event.getProperty("quitting");

		gameController.handleEndTurn(success, player, target, title, message, finished, isQuitting);
	}
}
