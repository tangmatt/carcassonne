package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TilePreview;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TilePreviewHandler implements EventHandler<MouseEvent> {
	private final TilePreview preview;
	private final GameController controller;
	
	public TilePreviewHandler(final TilePreview preview, final GameController controller) {
		this.preview = preview;
		this.controller = controller;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		if(!event.isPrimaryButtonDown())
			return;
		
		controller.getBoard().setSelectedPreviewTile(preview.getTile());
		//controller.handlePopOver(preview, event.getScreenX(), event.getScreenY());
		controller.handleHints(preview);
		preview.setSelected(true);
	}
}
