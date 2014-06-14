package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.LobbyController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;

public class StartReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		LobbyController lobbyController = gameController.getLobbyController();
		String[] names = (String[])event.getProperty("names");
		Boolean[] statusesObj = (Boolean[])event.getProperty("statuses");
		boolean[] statuses = new boolean[statusesObj.length];
		for(int i=0; i<statusesObj.length; ++i)
			statuses[i] = statusesObj[i].booleanValue();
		
		int index;
		for(index = 0; index<names.length; ++index) {
			if(names[index].equalsIgnoreCase(game.getPlayerName())) {
				gameController.getGameData().setIndex(index+1);
				break;
			}
		}

		lobbyController.close();
		gameController.updatePlayerPanel(names, statuses);
		gameController.updateFollowerPanel();
		gameController.startGame();
		gameController.getRootPane().setDisable(false);
	}
}
