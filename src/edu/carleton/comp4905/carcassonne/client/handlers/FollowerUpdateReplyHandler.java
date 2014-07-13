package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.GameData;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;

public class FollowerUpdateReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		
		String target = (String)event.getProperty("target");
		int followers = (int)event.getProperty("followers");
		
		// check if this client's name is the same as the target's name
		if(!game.getPlayerName().equalsIgnoreCase(target))
			return;

		for(int i=0; i<followers; ++i) {
			gameController.getGameData().increaseNumOfFollowers();
			if(gameController.getGameData().getNumOfFollowers() >= GameData.TOTAL_FOLLOWERS)
				break;
		}

		gameController.updateFollowerPanel();
	}
}