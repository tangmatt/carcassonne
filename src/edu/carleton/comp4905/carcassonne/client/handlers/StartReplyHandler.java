package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.GameData;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.Player;

public class StartReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String target = (String)event.getProperty("target");
		String[] names = (String[])event.getProperty("names");
		Mode mode = (Mode)event.getProperty("mode");
		GameData.ROWS = (int)event.getProperty("rows");
		GameData.COLS = (int)event.getProperty("columns");
		int row = (int)event.getProperty("row");
		int column = (int)event.getProperty("column");
		Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
		int index;
		
		game.getGameController().initGame();
		for(index = 0; index<names.length; ++index) {
			if(names[index].equalsIgnoreCase(game.getPlayerName())) {
				gameController.getGameData().setIndex(index+1);
				break;
			}
		}
		
		gameController.closeLobby();
		game.setMode(mode);
		gameController.getScoreData().setPlayerScore(game.getPlayerName(), 0);
		gameController.handleStartGame(names, statuses, target, row, column, mode);
	}
}
