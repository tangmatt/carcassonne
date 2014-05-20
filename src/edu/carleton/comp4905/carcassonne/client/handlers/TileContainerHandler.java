package edu.carleton.comp4905.carcassonne.client.handlers;

import edu.carleton.comp4905.carcassonne.client.AbstractTile;
import edu.carleton.comp4905.carcassonne.client.GameController;
import edu.carleton.comp4905.carcassonne.client.TileManager;
import edu.carleton.comp4905.carcassonne.client.TilePreview;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileContainerHandler implements EventHandler<MouseEvent> {
	private final AbstractTile container;
	private final GameController controller;
	private final TileManager tileManager;
	
	public TileContainerHandler(final AbstractTile container, final GameController controller) {
		this.container = container;
		this.controller = controller;
		this.tileManager = TileManager.getInstance();
	}
	
	@Override
	public void handle(final MouseEvent event) {
		if(!event.isPrimaryButtonDown())
			return;
		
		if(container.isEmpty())
			container.addTile(controller.getBoard().getSelectedPreviewTile());
		
		for(TilePreview preview : controller.getBoard().getPreviews()) {
			preview.setSelected(false);
			preview.removeMouseListener();
			preview.addTile(tileManager.getEmptyTile());
		}
		
		controller.endTurn();
	}
}
