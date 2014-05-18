package edu.carleton.comp4905.carcassonne.client;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class TilePreviewHandler implements EventHandler<MouseEvent> {
	private final TileBase preview;
	private final GameController controller;
	
	public TilePreviewHandler(TileBase container, GameController controller) {
		this.preview = container;
		this.controller = controller;
	}
	
	@Override
	public void handle(MouseEvent event) {
//		if(preview.getTile().getName().equals(StringConstants.EMPTY_TILE))
//			return;
//		
		for(Node node : controller.getPreviewPane().getChildren()) {
			TileBase temp = (TileBase)node;
			temp.setSelected(false);
		}
		
		for(Node node : controller.getGridPane().getChildren()) {
			TileBase temp = (TileBase)node;
			temp.setSelected(false);
		}
		
		controller.getBoard().setSelectedPreviewTile(preview.getTile());
		controller.handleHints(preview);
		preview.setSelected(true);
	}
}
