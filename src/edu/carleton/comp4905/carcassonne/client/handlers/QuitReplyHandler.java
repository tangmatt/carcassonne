package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.LobbyController;
import edu.carleton.comp4905.carcassonne.client.MessageDialog;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;

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
				Boolean[] statusesObj = (Boolean[])event.getProperty("statuses");
				boolean[] statuses = new boolean[statusesObj.length];
				for(int i=0; i<statusesObj.length; ++i)
					statuses[i] = statusesObj[i].booleanValue();
				String[] names = (String[])event.getProperty("names");
				boolean finished = (boolean)event.getProperty("finished");
				String message = (String)event.getProperty("message");
				boolean gameInProgress = (boolean)event.getProperty("gameInProgress");
				
				if(gameInProgress)
					gameController.updatePlayerPanel(names, statuses);
				else {
					lobbyController.updatePlayerIcons(numOfPlayers);
					lobbyController.handleStartAvailability(numOfPlayers);
					gameController.getScoreData().removePlayer(event.getPlayerName());
				}
				
				if(finished && message != null) {
					if(event.getPlayerName().equals(game.getPlayerName())) {
						gameController.getGameClient().getStage().getOnCloseRequest().handle(null);
					} else {
						gameController.blurGame(true);
						new MessageDialog(gameController.getGameClient().getStage(),
								gameController.getGameClient(),
								LocalMessages.getString("InfoTitle"),
								message,
								true)
						.show();
					}
				}
			}
		});
	}
}
