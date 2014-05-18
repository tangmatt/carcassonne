package edu.carleton.comp4905.carcassonne.client;

public class Board {
	public static final int ROWS = 9;
	public static final int COLS = 9;
	public static final int CENTER_ROW = Math.floorDiv(ROWS, 2);
	public static final int CENTER_COL = Math.floorDiv(COLS, 2);
	
	private TileContainer[][] tiles;
	private TilePreview[] previews;
	private GameTile selected;
	private TileManager tileManager;
	
	public Board() {
		tiles = new TileContainer[COLS][ROWS];
		previews = new TilePreview[Side.values().length];
		tileManager = TileManager.getInstance();
	}
	
	/**
	 * Sets the tile to the specified row and column.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param tile a TileContainer
	 */
	public void setTile(int r, int c, TileContainer tile) {
		tiles[c][r] = tile;
	}
	
	/**
	 * Returns the TileContainer at specified row and column.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @return TileContainer
	 */
	public TileContainer getTile(int r, int c) {
		return tiles[c][r];
	}
	
	public void setSelectedPreviewTile(GameTile selected) {
		this.selected = selected;
	}
	
	public GameTile getSelectedPreviewTile() {
		return selected;
	}
	
	public void setRotatedPreviews(GameTile tile) {
		for(int i=0, degrees=0; i<previews.length; ++i, degrees+=90) {
			try {
				previews[i] = new TilePreview(tileManager.getTile(tile.getName()+degrees));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public TilePreview get
}
