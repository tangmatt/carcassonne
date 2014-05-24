package edu.carleton.comp4905.carcassonne.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

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
				tiles.put(key, gameTile);
				addTileCombinations(key, top, right, bottom, left);
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
	 * @param key a key (String)
	 * @param gameTile a GameTile
	 * @param top a Segment
	 * @param right a Segment 
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	protected void addTileCombinations(final String key, final Segment top, final Segment right, final Segment bottom, final Segment left) {
		for(int i=1, degrees=90; i<Side.values().length; ++i, degrees+=90) {
			GameTile rotatedTile = new GameTile();
			if(degrees == 90)
				rotatedTile.setTile(key, left, top, right, bottom);
			else if(degrees == 180)
				rotatedTile.setTile(key, bottom, left, top, right);
			else if(degrees == 270)
				rotatedTile.setTile(key, right, bottom, left, top);
			rotatedTile.setRotate(degrees);
			tiles.put(key+degrees, rotatedTile);
		}
	}
	
	/**
	 * Returns a copy of the GameTile object mapped to the specified key.
	 * @param key a key
	 * @return a GameTile
	 */
	public GameTile getTile(final String key) {
		return (GameTile)((GameTile)tiles.get(key)).clone();
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
	
	/**
	 * Returns the rotated GameTile object of the given GameTile.
	 * @param degrees a degrees (Integer)
	 * @param tile a GameTile
	 * @return a GameTile
	 */
	public GameTile rotateTile(final double degrees, final GameTile tile) {
		GameTile clone = (GameTile)tile.clone();
		clone.setRotate(degrees);
		return clone;
	}
}
