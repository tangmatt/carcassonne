package edu.carleton.comp4905.carcassonne.client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.controlsfx.control.PopOver;

import edu.carleton.comp4905.carcassonne.common.TileManager;
import javafx.scene.image.ImageView;

public class GameData {
	public static int ROWS;
	public static int COLS;
	public static final int TOTAL_FOLLOWERS = 7;
	public static final int TOTAL_PLAYERS = 5;
	public static final int TILE_SIZE = 64;
	public static final int GAP_SIZE = 2;
	public static final int OFFSET = 30;
	
	private TileContainer[][] tiles;
	private final TilePreview[] previews;
	private GameTile selected;
	
	private final Map<TileContainer, String> tilesWithFollowers;
	private final Map<ImageView, PopOver> popOverMap;

	private ImageView[] followers, players;
	private int index;
	private int numOfFollowers;
	
	public GameData() {
		previews = new TilePreview[TileManager.TOTAL_ROTATED_VIEWS];
		tilesWithFollowers = new HashMap<TileContainer, String>();
		popOverMap = new LinkedHashMap<ImageView, PopOver>();
		players = new ImageView[TOTAL_PLAYERS];
		followers = new ImageView[TOTAL_FOLLOWERS];
		numOfFollowers = TOTAL_FOLLOWERS;
	}
	
	/**
	 * Initializes the tiles data storage.
	 */
	public void init() {
		tiles = new TileContainer[ROWS][COLS];
	}
	
	/**
	 * Sets the tile to the specified row and column.
	 * @param tile a TileContainer
	 * @param r the row
	 * @param c the column
	 */
	public void setTile(final TileContainer tile, final int r, final int c) {
		tiles[r][c] = tile;
	}
	
	/**
	 * Returns the TileContainer at specified row and column.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @return TileContainer
	 */
	public TileContainer getTile(final int r, final int c) {
		TileContainer tile = null;
		try {
			tile = tiles[r][c];
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return tile;
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
	
	/**
	 * Returns the row index of the specified tile.
	 * @param container the tile container
	 * @return the row
	 */
	public synchronized int getRowIndex(final TileContainer container) {
		for(int r=0; r<GameData.ROWS; ++r) {
			for(int c=0; c<GameData.COLS; ++c) {
				if(getTile(r, c).equals(container)) {
					return r;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Returns the column index of the specified tile.
	 * @param container the tile container
	 * @return the column
	 */
	public synchronized int getColumnIndex(final TileContainer container) {
		for(int r=0; r<GameData.ROWS; ++r) {
			for(int c=0; c<GameData.COLS; ++c) {
				if(getTile(r, c).equals(container)) {
					return c;
				}
			}
		}
		return -1;
	}
	
	/**
	 * Expands the current tile map with an empty column to the right.
	 */
	public void expandRightColumn() {
		TileContainer[][] newTiles = new TileContainer[ROWS][++COLS];
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				try {
					newTiles[r][c] = tiles[r][c];
				} catch(ArrayIndexOutOfBoundsException e) {
					newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
				}
			}
		}
		tiles = newTiles;
	}
	
	/**
	 * Expands the current tile map with an empty row to the bottom.
	 */
	public void expandBottomRow() {
		TileContainer[][] newTiles = new TileContainer[++ROWS][COLS];
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				try {
					newTiles[r][c] = tiles[r][c];
				} catch(ArrayIndexOutOfBoundsException e) {
					newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
				}
			}
		}
		tiles = newTiles;
	}
	
	/**
	 * Expands the current tile map with an empty column to the left.
	 */
	public void expandLeftColumn() {
		TileContainer[][] newTiles = new TileContainer[ROWS][++COLS];
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
			}
		}
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				try {
					newTiles[r][c+1] = tiles[r][c];
				} catch(ArrayIndexOutOfBoundsException e) {
					newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
				}
			}
		}
		tiles = newTiles;
	}
	
	/**
	 * Expands the current tile map with an empty row to the top.
	 */
	public void expandTopRow() {
		TileContainer[][] newTiles = new TileContainer[++ROWS][COLS];
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
			}
		}
		for(int r=0; r<ROWS; ++r) {
			for(int c=0; c<COLS; ++c) {
				try {
					newTiles[r+1][c] = tiles[r][c];
				} catch(ArrayIndexOutOfBoundsException e) {
					newTiles[r][c] = new TileContainer(TileManager.getInstance().getEmptyTile());
				}
			}
		}
		tiles = newTiles;
	}
}
