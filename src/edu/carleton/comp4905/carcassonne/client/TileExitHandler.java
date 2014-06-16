package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.TileManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileExitHandler implements EventHandler<MouseEvent> {
	private final TileManager tileManager;
	
	public TileExitHandler() {
		this.tileManager = TileManager.getInstance();
	}
	
	@Override
	public void handle(final MouseEvent event) {
		TileContainer container = (TileContainer) event.getSource();
		container.getChildren().clear();
		container.addTile(tileManager.getEmptyTile());
	}
}
