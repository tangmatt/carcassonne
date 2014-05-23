package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileContainerHandler implements EventHandler<MouseEvent> {
	private final AbstractTile container;
	private final GameController controller;
	private final int row;
	private final int column;
	
	public TileContainerHandler(final AbstractTile container, final GameController controller, final int row, final int column) {
		this.container = container;
		this.controller = controller;
		this.row = row;
		this.column = column;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		Game game = controller.getGameClient().getGame();
		Connection connection = controller.getGameClient().getGame().getConnection();
		
		if(!event.isPrimaryButtonDown())
			return;
		
		if(container.isEmpty())
			container.addTile(controller.getModel().getSelectedPreviewTile());
		
		controller.clearPreviews();
		
		// send tile placement event to server
		Event gameEvent = new Event(EventType.SEND_TILE_REQUEST, game.getPlayerName());
		gameEvent.addProperty("tile", container.getTile().getName());
		gameEvent.addProperty("rotation", ((Double)container.getTile().getRotate()).intValue());
		gameEvent.addProperty("row", row);
		gameEvent.addProperty("column", column);
		connection.sendEvent(gameEvent);
		
		controller.endTurn();
	}
}
