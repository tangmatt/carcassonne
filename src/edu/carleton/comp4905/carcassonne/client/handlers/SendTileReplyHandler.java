package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TileContainer;
import edu.carleton.comp4905.carcassonne.client.TileManager;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;

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
				
				String tile = (String)event.getProperty("tile");
				int row = (int)event.getProperty("row");
				int column = (int)event.getProperty("column");
				
				gameController.refreshGameTiles();
				TileContainer container = new TileContainer(tileManager.getTile(tile));
				gameController.addTile(row, column, container);	
				gameController.firePreviewTileEvent(true);
			}
		});
	}
}
