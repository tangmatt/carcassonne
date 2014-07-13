package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Player;

public class StartTurnReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String player = event.getPlayerName();
		String target = (String)event.getProperty("target");
		String tile = (String)event.getProperty("tile");
		int targetIndex = (int)event.getProperty("targetIndex");
		int tilesLeft = (int)event.getProperty("tilesLeft");
		Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
		
		gameController.handleStartTurn(player, tile, target, targetIndex, statuses.length, tilesLeft);
	}
}
