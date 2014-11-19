package edu.carleton.comp4905.carcassonne.client;

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
		GameTile selectedTile = controller.getGameData().getCopyOfSelectedPreviewTile();
		Position position = container.getFollowerPosition();
		
		if(!event.isPrimaryButtonDown())
			return;
		
		if(!container.isEmpty())
			return;

		if(position != null) {
			if(controller.getGameData().getNumOfFollowers() <= 0)
				return;
		}
		
		controller.handleTileSelect(selectedTile, container, row, column);
	}
}
