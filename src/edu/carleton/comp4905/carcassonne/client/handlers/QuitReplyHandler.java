package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.LobbyController;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Player;

public class QuitReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		LobbyController lobbyController = gameController.getLobbyController();
		int numOfPlayers = (int)event.getProperty("numOfPlayers");
		Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
		String[] names = (String[])event.getProperty("names");
		boolean finished = (boolean)event.getProperty("finished");
		String title = (String)event.getProperty("messageTitle");
		String message = (String)event.getProperty("message");
		boolean gameInProgress = (boolean)event.getProperty("gameInProgress");
		
		if(gameInProgress) {
			gameController.updatePlayerPanel(names, statuses);
			if(finished && message != null) {
				if(event.getPlayerName().equals(game.getPlayerName())) {
					gameController.getGameClient().getStage().getOnCloseRequest().handle(null);
				} else {
					gameController.sendEndGameRequest(title, message);
				}
			}
		}
		else {
			lobbyController.updatePlayerIcons(numOfPlayers);
			lobbyController.handleStartAvailability(numOfPlayers);
		}
	}
}
