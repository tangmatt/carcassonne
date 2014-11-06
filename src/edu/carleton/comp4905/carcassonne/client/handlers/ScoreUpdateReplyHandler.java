package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.Player;

public class ScoreUpdateReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String target = (String)event.getProperty("target");
		int score = (int)event.getProperty("points");
		Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
		String[] names = (String[])event.getProperty("names");
		Mode mode = (Mode)event.getProperty("mode");
		
		gameController.updatePlayerScore(target, score);
		gameController.updatePlayerPanel(names, statuses, mode);
	}
}
