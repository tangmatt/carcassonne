package edu.carleton.comp4905.carcassonne.client;

public class Board {
	public static final int ROWS = 9;
	public static final int COLS = 9;
	public static final int CENTER_ROW = Math.floorDiv(ROWS, 2);
	public static final int CENTER_COL = Math.floorDiv(COLS, 2);
	
	private final TileContainer[][] tiles;
	private final TilePreview[] previews;
	private GameTile selected;
	
	public Board() {
		tiles = new TileContainer[COLS][ROWS];
		previews = new TilePreview[Side.values().length];
	}
	
	/**
	 * Sets the tile to the specified row and column.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param tile a TileContainer
	 */
	public void setTile(final int r, final int c, final TileContainer tile) {
		tiles[c][r] = tile;
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
	 * Returns the selected preview tile.
	 * @return GameTile
	 */
	public GameTile getSelectedPreviewTile() {
		return selected;
	}
	
	/**
	 * Returns the array of previews.
	 * @return an array of TilePreview
	 */
	public TilePreview[] getPreviews() {
		return previews;
	}
}
