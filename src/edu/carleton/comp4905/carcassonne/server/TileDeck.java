package edu.carleton.comp4905.carcassonne.server;

import edu.carleton.comp4905.carcassonne.common.TileManager;

public class TileDeck extends Deck<String> {
	private TileManager tileManager = TileManager.getInstance();
	
	public TileDeck() {
		super();
		initialize();
		shuffle();
	}
	
	private void initialize() {
		for(String name : tileManager.getCounters().keySet()) {
			for(int i=0; i<tileManager.getTileCount(name); ++i) {
				tiles.add(name);
			}
		}
	}
}
