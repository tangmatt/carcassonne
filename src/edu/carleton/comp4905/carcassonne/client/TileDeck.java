package edu.carleton.comp4905.carcassonne.client;

public class TileDeck extends Deck<GameTile> {
	private TileManager tileManager = TileManager.getInstance();
	
	public TileDeck() {
		super();
		initialize();
	}
	
	private void initialize() {
		for(String name : tileManager.getCounters().keySet()) {
			for(int i=0; i<tileManager.getTileCount(name); ++i) {
				tiles.add(tileManager.getTile(name));
			}
		}
	}
}
