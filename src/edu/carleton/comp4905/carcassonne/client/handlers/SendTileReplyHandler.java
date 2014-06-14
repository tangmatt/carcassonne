package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TileContainer;
import edu.carleton.comp4905.carcassonne.client.TileManager;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.Position;

public class SendTileReplyHandler implements EventHandler {
	@Override
	public void handleEvent(final Event event) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Connection connection = (Connection)event.getProperty("connection");
				Game game = (Game)connection.getService();
				GameController gameController = game.getGameController();
				TileManager tileManager = TileManager.getInstance();
				
				String player = (String)event.getPlayerName();
				String tile = (String)event.getProperty("tile");
				int row = (int)event.getProperty("row");
				int column = (int)event.getProperty("column");
				int meeple = (int)event.getProperty("meeple");
				Position position = (Position)event.getProperty("position");
				boolean shield = (boolean)event.getProperty("shield");
				
				gameController.startTurn();
				gameController.refreshGameTiles();
				TileContainer container = new TileContainer(tileManager.getTile(tile), row, column);
				container.getTile().setShield(shield);
				if(position != null) {
					container.addFollower(position, meeple, player);
					container.setFollower(position, player);
					gameController.getGameData().getTilesWithFollowers().put(container, player);
					gameController.updateConnectedSegments(container, position, container.getSegment(position), player);
				}
				
				gameController.addTile(container);
				gameController.updateFollowers();
				gameController.updateGameBoard();
				gameController.firePreviewTileEvent(true);
			}
		});
	}
}
