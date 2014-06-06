package edu.carleton.comp4905.carcassonne.client;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TilePreviewHandler implements EventHandler<MouseEvent> {
	private final GameController controller;
	
	public TilePreviewHandler(final GameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		TilePreview preview = (TilePreview) event.getSource();
		
		if(!event.isPrimaryButtonDown())
			return;

		controller.getModel().setSelectedPreviewTile(preview.getTile());
		controller.handleHints(preview);
		preview.setSelected(true);
	}
}
