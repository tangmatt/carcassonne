package edu.carleton.comp4905.carcassonne.client;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class TileContainerHandler implements EventHandler<MouseEvent> {
	private final TileBase container;
	private final GameController controller;
	private final TileManager tileManager;
	
	public TileContainerHandler(TileBase container, GameController controller) {
		this.container = container;
		this.controller = controller;
		this.tileManager = TileManager.getInstance();
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(event.isPrimaryButtonDown()) {
			if(container.isEmpty()) {
				container.addTile(controller.getBoard().getSelectedPreviewTile());
			}
		}
		
		for(Node node : controller.getPreviewPane().getChildren()) {
			TileBase temp = (TileBase)node;
			temp.setSelected(false);
			try {
				temp.addTile(tileManager.getStarterTile());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
}
