package edu.carleton.comp4905.carcassonne.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.StringConstants;

public class TileManager {
	private static volatile TileManager instance = null;
	private Map<String, GameTile> tiles;
	public static final String CONFIG_DIR = "config/tiles.ini";
	public static int NUM_OF_TILES = 0;
	
	private TileManager() {
		tiles = new HashMap<String, GameTile>();
		initialize();
	}
	
	// ensures only one instance of this class (Singleton)
	public static TileManager getInstance() {
		if(instance == null) {
			synchronized(TileManager.class) {
				instance = new TileManager();
			}
		}
		return instance;
	}
	
	/**
	 * Loads the INI file containing data for each tile.
	 */
	protected void initialize() {
		try {
			Ini ini = new Ini();
			ini.load(new FileInputStream(CONFIG_DIR));
			for(String key : ini.keySet()) {
				Section section = ini.get(key);
				Segment top = Segment.valueOf(section.get(Side.TOP.toString()));
				Segment right = Segment.valueOf(section.get(Side.RIGHT.toString()));
				Segment bottom = Segment.valueOf(section.get(Side.BOTTOM.toString()));
				Segment left = Segment.valueOf(section.get(Side.LEFT.toString()));
				GameTile gameTile = new GameTile();
				gameTile.setTile(key, top, right, bottom, left);
				if(section.get("FOLLOWERS") != null) {
					String followers = section.get("FOLLOWERS").replace(" ", "");
					if(followers.contains(",")) {
						String[] splitted = followers.split(",");
						for(int i=0; i<splitted.length; ++i) {
							Position pos = Position.valueOf(splitted[i]);
							gameTile.addPosition(pos);
						}
					} else {
						Position pos = Position.valueOf(followers);
						gameTile.addPosition(pos);
					}				
				}
				tiles.put(key, gameTile);
				addTileCombinations(gameTile, top, right, bottom, left);
				if(!(key.equals(StringConstants.EMPTY_TILE) || key.equals(StringConstants.STARTER_TILE)))
					NUM_OF_TILES++;
			}
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the original tile and its various rotations.
	 * @param gameTile a GameTile
	 * @param top a Segment
	 * @param right a Segment 
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	protected void addTileCombinations(final GameTile tile, final Segment top, final Segment right, final Segment bottom, final Segment left) {
		for(int i=1, degrees=90; i<Side.values().length; ++i, degrees+=90) {
			GameTile rotatedTile = new GameTile(tile);
			rotatedTile.rotate(degrees);
			tiles.put(tile.getName()+degrees, rotatedTile);
		}
	}
	
	/**
	 * Returns a copy of the GameTile object mapped to the specified key.
	 * @param key a key
	 * @return a GameTile
	 */
	public GameTile getTile(final String key) {
		return new GameTile(tiles.get(key));
	}
	
	/**
	 * Returns a copy of the GameTile object represented as an empty tile.
	 * @return a GameTile
	 */
	public GameTile getEmptyTile() {
		return getTile(StringConstants.EMPTY_TILE);
	}
	
	/**
	 * Returns a copy of the GameTile object represented as the starting tile.
	 * @return a GameTile
	 */
	public GameTile getStarterTile() {
		return getTile(StringConstants.STARTER_TILE);
	}
}
