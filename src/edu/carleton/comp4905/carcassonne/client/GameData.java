package edu.carleton.comp4905.carcassonne.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.controlsfx.control.PopOver;

import edu.carleton.comp4905.carcassonne.common.TileManager;
import javafx.scene.image.ImageView;

public class GameData {
	public static final int ROWS = 8;
	public static final int COLS = 9;
	public static final int TOTAL_FOLLOWERS = 7;
	public static final int TOTAL_PLAYERS = 5;
	
	private final TileContainer[][] tiles;
	private final TilePreview[] previews;
	private GameTile selected;
	
	private final Map<TileContainer, String> tilesWithFollowers;
	private final Map<ImageView, PopOver> popOverMap;

	private ImageView[] followers, players;
	private int index;
	private int numOfFollowers;
	
	public GameData() {
		tiles = new TileContainer[COLS][ROWS];
		previews = new TilePreview[TileManager.TOTAL_ROTATED_VIEWS];
		tilesWithFollowers = new HashMap<TileContainer, String>();
		popOverMap = new LinkedHashMap<ImageView, PopOver>();
		players = new ImageView[TOTAL_PLAYERS];
		followers = new ImageView[TOTAL_FOLLOWERS];
		numOfFollowers = TOTAL_FOLLOWERS;
	}
	
	/**
	 * Sets the tile to the specified row and column.
	 * @param tile a TileContainer
	 */
	public void setTile(final TileContainer tile) {
		tiles[tile.c][tile.r] = tile;
	}
	
	/**
	 * Returns the TileContainer at specified row and column.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @return TileContainer
	 */
	public TileContainer getTile(final int r, final int c) {
		return tiles[c][r];
	}
	
	/**
	 * Set selected preview tile.
	 * @param selected
	 */
	public void setSelectedPreviewTile(final GameTile selected) {
		this.selected = selected;
	}
	
	/**
	 * Adds the player view and pop over to the map.
	 * @param imageView an array of ImageViews
	 * @param popOver a PopOver
	 */
	public void addPopOvers(final ImageView[] imageView, final PopOver popOver) {
		for(int i=0; i<imageView.length; ++i)
			popOverMap.put(imageView[i], popOver);
	}
	
	/**
	 * Adds the follower view.
	 * @param imageView an array of ImageViews
	 */
	public void addFollowerViews(final ImageView[] imageView) {
		for(int i=0; i<imageView.length; ++i)
			followers[i] = imageView[i];
	}
	
	/**
	 * Returns the selected preview tile.
	 * @return GameTile
	 */
	public GameTile getSelectedPreviewTile() {
		return selected;
	}
	
	/**
	 * Returns a copy of the selected preview tile.
	 * @return GameTile
	 */
	public GameTile getCopyOfSelectedPreviewTile() {
		return new GameTile(selected);
	}
	
	/**
	 * Returns the array of previews.
	 * @return an array of TilePreview
	 */
	public TilePreview[] getPreviews() {
		return previews;
	}
	
	/**
	 * Returns a map of image views mapped to their pop overs.
	 * @return a Map<ImageView, PopOver>
	 */
	public Map<ImageView, PopOver> getPopOverViews() {
		return popOverMap;
	}
	
	/**
	 * Returns the images representing the followers.
	 * @return an array of follower views
	 */
	public ImageView[] getFollowerViews() {
		return followers;
	}
	
	/**
	 * Returns the images representing the players.
	 * @return an array of player views
	 */
	public ImageView[] getPlayerViews() {
		return players;
	}
	
	/**
	 * Sets the index.
	 * @param index an Integer
	 */
	public void setIndex(final int index) {
		this.index = index;
	}
	
	/**
	 * Returns the index related to the image naming for followers.
	 * @return an Integer
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Decrements the number of followers available.
	 */
	public void decreaseNumOfFollowers() {
		numOfFollowers--;
	}
	
	/**
	 * Increments the number of followers available.
	 */
	public void increaseNumOfFollowers() {
		numOfFollowers++;
	}
	
	/**
	 * Returns the number of followers available.
	 * @return the number of followers
	 */
	public int getNumOfFollowers() {
		return numOfFollowers;
	}
	
	/**
	 * Returns a map of tiles with followers with their owner name.
	 * @return the map of tiles with followers with their owner name
	 */
	public Map<TileContainer, String> getTilesWithFollowers() {
		return tilesWithFollowers;
	}
}
