package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileContainerHandler implements EventHandler<MouseEvent> {
	private final GameController controller;
	private final int row;
	private final int column;
	
	public TileContainerHandler(final GameController controller, final int row, final int column) {
		this.controller = controller;
		this.row = row;
		this.column = column;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		TileContainer container = (TileContainer) event.getSource();
		Game game = controller.getGameClient().getGame();
		Connection connection = controller.getGameClient().getGame().getConnection();
		GameTile selectedTile = controller.getGameData().getCopyOfSelectedPreviewTile();
		Position position = container.getFollowerPosition();
		container.setHoverTile(false);
		
		if(!event.isPrimaryButtonDown())
			return;
		
		if(position != null) {
			if(controller.getGameData().getNumOfFollowers() <= 0)
				return;
			controller.getGameData().decreaseNumOfFollowers();
			controller.updateFollowerPanel();
		}
		
		// send tile placement event to server
		Event gameEvent = new Event(EventType.SEND_TILE_REQUEST, game.getPlayerName());
		gameEvent.addProperty("tile", selectedTile.getName());
		gameEvent.addProperty("rotation", ((Double)selectedTile.getRotate()).intValue());
		gameEvent.addProperty("row", row);
		gameEvent.addProperty("column", column);
		gameEvent.addProperty("meeple", controller.getGameData().getIndex());
		gameEvent.addProperty("position", container.getFollowerPosition());
		gameEvent.addProperty("shield", container.getTile().hasShield());
		connection.sendEvent(gameEvent);
		
		controller.endTurn();
	}
}
