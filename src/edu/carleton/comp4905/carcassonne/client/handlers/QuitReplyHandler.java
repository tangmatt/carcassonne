package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.LobbyController;
import edu.carleton.comp4905.carcassonne.client.MessageDialog;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.StringConstants;

public class QuitReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Connection connection = (Connection)event.getProperty("connection");
				Game game = (Game)connection.getService();
				GameController gameController = game.getGameController();
				LobbyController lobbyController = gameController.getLobbyController();
				int numOfPlayers = (int)event.getProperty("numOfPlayers");
				boolean[] statuses = (boolean[])event.getProperty("statuses");
				String[] names = (String[])event.getProperty("playerNames");
				boolean finished = (boolean)event.getProperty("finished");
				String message = (String)event.getProperty("message");
				boolean gameInProgress = (boolean)event.getProperty("gameInProgress");
				
				if(gameInProgress)
					gameController.updatePlayerPanel(names, statuses);
				else
					lobbyController.updatePlayerIcons(numOfPlayers);
				
				if(finished && message != null) {
					gameController.blurGame(true);
					new MessageDialog(gameController.getGridPane().getScene().getWindow(),
							gameController.getGameClient(),
							StringConstants.INFO_TITLE,
							message)
					.show();
				}
			}
		});
	}
}
