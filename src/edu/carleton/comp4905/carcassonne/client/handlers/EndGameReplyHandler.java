package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.MessageDialog;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;

public class EndGameReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String messageTitle = (String)event.getProperty("messageTitle");
		String message = (String)event.getProperty("message");
		
		gameController.stopKeepAliveTimer();
		gameController.blurGame(true);
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				new MessageDialog(gameController.getGridPane().getScene().getWindow(),
						gameController.getGameClient(),
						messageTitle,
						message,
						true)
				.show();
			}
		});
	}
}
