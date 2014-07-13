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
		GameTile selectedTile = controller.getGameData().getCopyOfSelectedPreviewTile();
		int index = controller.getGameData().getIndex();
		Map<Position, String> positions = selectedTile.getPositions();
		/*String playerName = controller.getGameClient().getGame().getPlayerName();
		int r = container.r, c = container.c;
		GameData gameData = controller.getGameData();*/
		
		selectedTile.setOpacity(0.6f);
		container.addTile(selectedTile);
		
		Iterator<Position> keys = positions.keySet().iterator();
		while(keys.hasNext()) {
			Position pos = keys.next();
			if(positions.get(pos) != null && positions.get(pos).isEmpty()) {
				container.showFollower(pos, index);
				/*controller.updateConnectedSegments(container, pos, container.getSegment(pos), controller.getGameClient().getGame().getPlayerName());
				if(r-1 >= 0 && gameData.getTile(r-1, c).getTile().getPositions().get(Position.BOTTOM) != null 
						&& gameData.getTile(r-1, c).getTile().getPositions().get(Position.BOTTOM).equals(playerName))
					container.showFollower(pos, index);
				if(r+1 < 9 && gameData.getTile(r+1, c).getTile().getPositions().get(Position.TOP) != null  
						&& gameData.getTile(r+1, c).getTile().getPositions().get(Position.TOP).equals(playerName))
					container.showFollower(pos, index);
				if(c-1 >= 0 && gameData.getTile(r, c-1).getTile().getPositions().get(Position.RIGHT) != null 
						&& gameData.getTile(r, c-1).getTile().getPositions().get(Position.RIGHT).equals(playerName))
					container.showFollower(pos, index);
				if(c+1 < 9 && gameData.getTile(r, c+1).getTile().getPositions().get(Position.LEFT) != null 
						&& gameData.getTile(r, c+1).getTile().getPositions().get(Position.LEFT).equals(playerName))
					container.showFollower(pos, index);*/
			}
		}
	}
}
