package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
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
		GameTile selectedTile = controller.getModel().getCopyOfSelectedPreviewTile();
		
		if(!event.isPrimaryButtonDown())
			return;
		
		if(container.getFollowerPosition() != null) {
			if(controller.getModel().getNumOfFollowers() <= 0)
				return;
			controller.getModel().decreaseNumOfFollowers();
			controller.updateFollowerPanel();
		}
		
		// send tile placement event to server
		Event gameEvent = new Event(EventType.SEND_TILE_REQUEST, game.getPlayerName());
		gameEvent.addProperty("tile", selectedTile.getName());
		gameEvent.addProperty("rotation", ((Double)selectedTile.getRotate()).intValue());
		gameEvent.addProperty("row", row);
		gameEvent.addProperty("column", column);
		gameEvent.addProperty("meeple", controller.getModel().getIndex());
		gameEvent.addProperty("position", container.getFollowerPosition());
		connection.sendEvent(gameEvent);
		
		controller.endTurn();
		controller.startTurn();
	}
}
