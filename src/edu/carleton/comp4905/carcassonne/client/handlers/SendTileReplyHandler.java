package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TileContainer;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.TileManager;

public class SendTileReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		Connection connection = (Connection)event.getProperty("connection");
		Game game = (Game)connection.getService();
		GameController gameController = game.getGameController();
		TileManager tileManager = TileManager.getInstance();
		
		String player = event.getPlayerName();
		String tile = (String)event.getProperty("tile");
		int row = (int)event.getProperty("row");
		int column = (int)event.getProperty("column");
		int meeple = (int)event.getProperty("meeple");
		Position position = (Position)event.getProperty("position");
		boolean shield = (boolean)event.getProperty("shield");
		
		if(game.getPlayerName().equalsIgnoreCase(player) && position != null) {
			if(gameController.getGameData().getNumOfFollowers() <= 0)
				return;
			gameController.getGameData().decreaseNumOfFollowers();
			gameController.updateFollowerPanel();
		}
		
		gameController.refreshGameTiles();
		TileContainer container = new TileContainer(tileManager.getTile(tile));
		container.getTile().setShield(shield);
		if(position != null) {
			container.addFollower(position, meeple, player);
			container.setFollower(position, player);
			gameController.getGameData().getTilesWithFollowers().put(container, player);
			gameController.updateConnectedSegments(container, position, player);
		}

		gameController.addTile(container, row, column);
		gameController.updateFollowers();
		gameController.handleCompleteSegments(player);
	}
}
