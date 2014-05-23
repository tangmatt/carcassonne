package edu.carleton.comp4905.carcassonne.client;

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
		
		controller.getModel().setSelectedPreviewTile(preview.getTile());
		//controller.handlePopOver(preview, event.getScreenX(), event.getScreenY());
		controller.handleHints(preview);
		preview.setSelected(true);
	}
}
