package edu.carleton.comp4905.carcassonne.client;

import java.io.FileInputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Position;

public class TileManager {
	private static volatile TileManager instance = null;
	private Map<String, GameTile> tiles;
	private Map<String, Integer> counters;
	public static final String CONFIG_DIR = "config/tiles.ini";
	public static int NUM_OF_TILES = 0;
	public static final int TOTAL_ROTATED_VIEWS = 4;
	
	private TileManager() {
		tiles = new HashMap<String, GameTile>();
		counters = new HashMap<String, Integer>();
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
				Map<Position, Segment> segments = getSegments(section);
				GameTile gameTile = new GameTile();
				gameTile.setTile(key, segments);
				gameTile.setShield(section.get("SHIELD") != null && section.get("SHIELD").equalsIgnoreCase("TRUE"));
				if(section.get("FOLLOWERS") != null) {
					String followers = section.get("FOLLOWERS").replace(" ", "");
					if(followers.contains(",")) {
						String[] splitted = followers.split(",");
						for(int i=0; i<splitted.length; ++i) {
							Position pos = Position.valueOf(splitted[i]);
							gameTile.setPosition(pos, "");
						}
					} else {
						Position pos = Position.valueOf(followers);
						gameTile.setPosition(pos, "");
					}				
				}
				tiles.put(key, gameTile);
				counters.put(key, Integer.parseInt(section.get("COUNT")));
				addTileCombinations(gameTile);
				if(!(key.equals(LocalMessages.getString("EmptyTile")) || key.equals(LocalMessages.getString("StarterTile"))))
					NUM_OF_TILES++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a map of segments mapped to their positions.
	 * @param section the section
	 * @return the map of segments to positions
	 */
	protected Map<Position, Segment> getSegments(final Section section) {
		Map<Position, Segment> segments = new EnumMap<Position, Segment>(Position.class);
		segments.put(Position.TOP, Segment.valueOf(section.get(Position.TOP.toString())));
		segments.put(Position.RIGHT, Segment.valueOf(section.get(Position.RIGHT.toString())));
		segments.put(Position.BOTTOM, Segment.valueOf(section.get(Position.BOTTOM.toString())));
		segments.put(Position.LEFT, Segment.valueOf(section.get(Position.LEFT.toString())));
		segments.put(Position.CENTER, Segment.valueOf(section.get(Position.CENTER.toString())));
		segments.put(Position.TOP_LEFT, Segment.valueOf(section.get(Position.TOP_LEFT.toString())));
		segments.put(Position.TOP_RIGHT, Segment.valueOf(section.get(Position.TOP_RIGHT.toString())));
		segments.put(Position.BOTTOM_LEFT, Segment.valueOf(section.get(Position.BOTTOM_LEFT.toString())));
		segments.put(Position.BOTTOM_RIGHT, Segment.valueOf(section.get(Position.BOTTOM_RIGHT.toString())));
		return segments;
	}
	
	/**
	 * Adds the original tile and its various rotations.
	 * @param gameTile a GameTile
	 */
	protected void addTileCombinations(final GameTile tile) {
		for(int i=1, degrees=90; i<TOTAL_ROTATED_VIEWS; ++i, degrees+=90) {
			GameTile rotatedTile = new GameTile(tile);
			rotatedTile.setName(tile.getName() + degrees);
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
		return getTile(LocalMessages.getString("EmptyTile"));
	}
	
	/**
	 * Returns a copy of the GameTile object represented as the starting tile.
	 * @return a GameTile
	 */
	public GameTile getStarterTile() {
		return getTile(LocalMessages.getString("StarterTile"));
	}
	
	/**
	 * Returns the tiles mapped to their name.
	 * @return the tiles mapped to their name
	 */
	public Map<String, GameTile> getTiles() {
		return tiles;
	}
	
	/**
	 * Returns the map containing the count associated with each tile
	 * @return the map containing the count associated with each tile
	 */
	public Map<String, Integer> getCounters() {
		return counters;
	}
	
	/**
	 * Returns the number of repetitions of specified tile.
	 * @param key the tile name
	 * @return the number of repetitions of specified tile
	 */
	public int getTileCount(final String key) {
		return counters.get(key);
	}
}
