package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.LobbyController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
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
				boolean[] statuses = (boolean[])event.getProperty("statuses");
				
				gameController.updatePlayerPanel(statuses);
			}
		});
	}
}
