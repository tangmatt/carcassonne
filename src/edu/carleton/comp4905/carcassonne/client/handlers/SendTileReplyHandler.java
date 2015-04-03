package edu.carleton.comp4905.carcassonne.client.handlers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TileContainer;
import edu.carleton.comp4905.carcassonne.common.CommonUtil;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.Logger;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.TileManager;

public class SendTileReplyHandler implements edu.carleton.comp4905.carcassonne.common.EventHandler {
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
		Position shield = (Position)event.getProperty("shield");
		
		if(game.getPlayerName().equalsIgnoreCase(player) && position != null) {
			if(gameController.getGameData().getNumOfFollowers() <= 0)
				return;
			gameController.getGameData().decreaseNumOfFollowers();
			gameController.updateFollowerPanel();
		}
		
		gameController.refreshGameTiles();
		TileContainer container = new TileContainer(tileManager.getTile(tile));
		container.getTile().setShield(shield);

		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				if(position != null) {
					container.addFollower(position, meeple, player);
					container.setFollower(position, player);
					container.getTile().updateSegmentOwners(position, player, CommonUtil.getUniqueId());
					gameController.getGameData().getTilesWithFollowers().put(container, player);
					gameController.updateOwnedSegments(container.getPositionId(position), container);
				}
				gameController.addTile(container, row, column);
				gameController.updateFollowers();
				gameController.updateTiles(row, column);
				gameController.updateTiles(row, column);  // re-scan the board 
				gameController.handleCompleteSegments(player);
				gameController.handleScoring(player);

				container.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						Logger.log(container.getTile().getName());
						for (Position key : container.getTile().getPositions().keySet()) {
							Logger.log(key + " (" + container.getSegment(key) + ")" + " = " + container.getPositionData(key));
						}
					}
				});
				container.setLastPlacedTileStyle();
			}
		});
	}
}
