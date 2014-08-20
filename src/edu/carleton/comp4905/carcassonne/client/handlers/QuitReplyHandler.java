package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.Player;

public class QuitReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		int numOfPlayers = (int)event.getProperty("numOfPlayers");
		Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
		String[] names = (String[])event.getProperty("names");
		boolean finished = (boolean)event.getProperty("finished");
		String title = (String)event.getProperty("messageTitle");
		String message = (String)event.getProperty("message");
		boolean gameInProgress = (boolean)event.getProperty("gameInProgress");
		Mode mode = (Mode)event.getProperty("mode");
		
		gameController.handleQuitEvent(gameInProgress, finished, title,
				message, event.getPlayerName(), names, statuses, numOfPlayers, mode);
	}
}
