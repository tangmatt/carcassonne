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
	
	private TileManager() {
		tiles = new HashMap<String, GameTile>();
		initialize();
	}
	
	public static TileManager getInstance() {
		if(instance == null) {
			synchronized(TileManager.class) {
				instance = new TileManager();
			}
		}
		return instance;
	}
	
	protected void initialize() {
		Ini ini = new Ini();
		try {
			ini.load(new FileInputStream("config/tiles.ini"));
			for(String key : ini.keySet()) {
				Section section = ini.get(key);
				Segment top = Segment.valueOf(section.get(Side.TOP.toString()));
				Segment right = Segment.valueOf(section.get(Side.RIGHT.toString()));
				Segment bottom = Segment.valueOf(section.get(Side.BOTTOM.toString()));
				Segment left = Segment.valueOf(section.get(Side.LEFT.toString()));
				GameTile gameTile = new GameTile();
				gameTile.setTile(key, top, right, bottom, left);
				addTileCombinations(key, gameTile, top, right, bottom, left);
			}
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	protected void addTileCombinations(String key, GameTile gameTile, Segment top, Segment right, Segment bottom, Segment left) throws CloneNotSupportedException {
		tiles.put(key, gameTile);
		for(int i=0, degrees=90; i<Side.values().length; ++i, degrees+=90) {
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
	
	public GameTile getTile(String key) throws CloneNotSupportedException {
		return (GameTile)((GameTile)tiles.get(key)).clone();
	}
	
	public GameTile getEmptyTile() throws CloneNotSupportedException {
		return (GameTile)((GameTile)tiles.get(StringConstants.EMPTY_TILE)).clone();
	}
	
	public GameTile getStarterTile() throws CloneNotSupportedException {
		return (GameTile)((GameTile)tiles.get(StringConstants.STARTER_TILE)).clone();
	}
	
	public GameTile rotateTile(double degrees, GameTile tile) throws CloneNotSupportedException {
		GameTile clone = (GameTile)tile.clone();
		clone.setRotate(degrees);
		return clone;
	}
}
