package edu.carleton.comp4905.carcassonne.client;

import java.util.Iterator;
import java.util.Map;

import edu.carleton.comp4905.carcassonne.common.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileHoverHandler implements EventHandler<MouseEvent> {
	private final GameController controller;
	
	public TileHoverHandler(final GameController controller) {
		this.controller = controller;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		TileContainer container = (TileContainer) event.getSource();		
		GameTile selectedTile = controller.getModel().getCopyOfSelectedPreviewTile();
		int index = controller.getModel().getIndex();
		Map<Position, Boolean> positions = selectedTile.getPositions();
		
		selectedTile.setOpacity(0.6f);
		container.addTile(selectedTile);
		Iterator<Position> keys = positions.keySet().iterator();
		while(keys.hasNext()) {
			Position pos = keys.next();
			if(positions.get(pos) != null && positions.get(pos))
				container.addFollower(pos, index, false);
		}
	}
}
