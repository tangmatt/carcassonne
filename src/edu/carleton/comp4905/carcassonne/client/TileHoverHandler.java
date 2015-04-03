package edu.carleton.comp4905.carcassonne.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		Map<Position, PositionData> positions = selectedTile.getPositions();
		String playerName = controller.getGameClient().getGame().getPlayerName();
		GameData gameData = controller.getGameData();
		int r = gameData.getRowIndex(container), c = gameData.getColumnIndex(container);
		
		selectedTile.setOpacity(0.6f);
		container.addTile(selectedTile);
		
		Iterator<Position> keys = positions.keySet().iterator();
		while(keys.hasNext()) {
			Position pos = keys.next();
			Set<Position> outerPositions = new GameTile(container.getTile()).updateSegmentOwners(pos, playerName, -1);
			if(outerPositions.size() == 0) {
				if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
					container.showFollower(pos, index);
				}
			}
			int count = 0;
			for(int i=0; i<outerPositions.toArray().length; ++i) {
				Position outer = (Position) outerPositions.toArray()[i];
				if(outer == Position.TOP && (gameData.getTile(r-1, c) == null || (gameData.getTile(r-1, c) != null && 
						(gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM) == null ||
						 gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.BOTTOM && (gameData.getTile(r+1, c) == null || (gameData.getTile(r+1, c) != null && 
						(gameData.getTile(r+1, c).getPositionOwner(Position.TOP) == null ||
						 gameData.getTile(r+1, c).getPositionOwner(Position.TOP).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.LEFT && (gameData.getTile(r, c-1) == null || (gameData.getTile(r, c-1) != null && 
						(gameData.getTile(r, c-1).getPositionOwner(Position.RIGHT) == null ||
						 gameData.getTile(r, c-1).getPositionOwner(Position.RIGHT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.RIGHT && (gameData.getTile(r, c+1) == null || (gameData.getTile(r, c+1) != null &&
						(gameData.getTile(r, c+1).getPositionOwner(Position.LEFT) == null ||
						 gameData.getTile(r, c+1).getPositionOwner(Position.LEFT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.TOP_RIGHT_RIGHT && (gameData.getTile(r, c+1) == null || (gameData.getTile(r, c+1) != null &&
						(gameData.getTile(r, c+1).getPositionOwner(Position.TOP_LEFT_LEFT) == null ||
						 gameData.getTile(r, c+1).getPositionOwner(Position.TOP_LEFT_LEFT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.BOTTOM_RIGHT_RIGHT && (gameData.getTile(r, c+1) == null || (gameData.getTile(r, c+1) != null &&
						(gameData.getTile(r, c+1).getPositionOwner(Position.BOTTOM_LEFT_LEFT) == null ||
						 gameData.getTile(r, c+1).getPositionOwner(Position.BOTTOM_LEFT_LEFT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.TOP_LEFT_LEFT && (gameData.getTile(r, c-1) == null || (gameData.getTile(r, c-1) != null && 
						(gameData.getTile(r, c-1).getPositionOwner(Position.TOP_RIGHT_RIGHT) == null ||
						 gameData.getTile(r, c-1).getPositionOwner(Position.TOP_RIGHT_RIGHT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.BOTTOM_LEFT_LEFT && (gameData.getTile(r, c-1) == null || (gameData.getTile(r, c-1) != null && 
						(gameData.getTile(r, c-1).getPositionOwner(Position.BOTTOM_RIGHT_RIGHT) == null ||
						 gameData.getTile(r, c-1).getPositionOwner(Position.BOTTOM_RIGHT_RIGHT).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.TOP_LEFT_TOP && (gameData.getTile(r-1, c) == null || (gameData.getTile(r-1, c) != null && 
						(gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM_LEFT_BOTTOM) == null ||
						 gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM_LEFT_BOTTOM).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.TOP_RIGHT_TOP && (gameData.getTile(r-1, c) == null || (gameData.getTile(r-1, c) != null && 
						(gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM_RIGHT_BOTTOM) == null ||
						 gameData.getTile(r-1, c).getPositionOwner(Position.BOTTOM_RIGHT_BOTTOM).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.BOTTOM_LEFT_BOTTOM && (gameData.getTile(r+1, c) == null || (gameData.getTile(r+1, c) != null && 
						(gameData.getTile(r+1, c).getPositionOwner(Position.TOP_LEFT_TOP) == null ||
						 gameData.getTile(r+1, c).getPositionOwner(Position.TOP_LEFT_TOP).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
				else if(outer == Position.BOTTOM_RIGHT_BOTTOM && (gameData.getTile(r+1, c) == null || (gameData.getTile(r+1, c) != null && 
						(gameData.getTile(r+1, c).getPositionOwner(Position.TOP_RIGHT_TOP) == null ||
						 gameData.getTile(r+1, c).getPositionOwner(Position.TOP_RIGHT_TOP).equals(""))))) {
					if(selectedTile.getPositionOwner(pos) != null && selectedTile.getPositionOwner(pos).isEmpty()) {
						count++;
					}
				}
			}

			if(count == outerPositions.size())
				container.showFollower(pos, index);
		}
	}
}
