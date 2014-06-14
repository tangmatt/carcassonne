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

public class JoinReplyHandler implements EventHandler {
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
				boolean success = (boolean)event.getProperty("success");
				String message = (String)event.getProperty("message");
				
				if(success) {
					lobbyController.updatePlayerIcons(numOfPlayers);
					lobbyController.handleStartAvailability(numOfPlayers);
					gameController.showLobbyDialog();
					gameController.addPlayerScore(event.getPlayerName(), 0);
				} else {
					gameController.blurGame(true);
					new MessageDialog(gameController.getGridPane().getScene().getWindow(),
							gameController.getGameClient(),
							LocalMessages.getString("RefusedTitle"),
							message,
							true)
					.show();
				}
			}
		});
	}
}
