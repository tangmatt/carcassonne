package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.MessageDialog;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;

public class JoinReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		int numOfPlayers = (int)event.getProperty("numOfPlayers");
		boolean success = (boolean)event.getProperty("success");
		String message = (String)event.getProperty("message");
		
		if(success) {
			gameController.updateLobbyUI(numOfPlayers);
			gameController.showLobbyDialog();
		} else {
			gameController.blurGame(true);
			PlatformManager.run(new Runnable() {
				@Override
				public void run() {
					new MessageDialog(gameController.getGridPane().getScene().getWindow(),
							gameController.getGameClient(),
							LocalMessages.getString("RefusedTitle"),
							message,
							true)
					.show();
				}
			});
		}
	}
}
