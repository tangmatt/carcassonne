package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class AbstractTile extends StackPane {
	protected GameTile tile;
	protected EventHandler<MouseEvent> pressHandler;
	public static final String selectedStyle = "-fx-border-color: #2F8BFA;"
            + "-fx-border-width: 2;"
			+ "-fx-border-insets: -1;"
            + "-fx-border-style: solid;";
	public static final String latestStyle = "-fx-border-color: #708090;"
            + "-fx-border-width: 2;"
			+ "-fx-border-insets: -1;"
            + "-fx-border-style: solid;"
			+ "-fx-opacity: 0.7";
	
	public AbstractTile(final GameTile tile) {
		addTile(tile);
		setSelected(false);
	}
	
	/**
	 * Adds a new GameTile above the existing GameTile(s).
	 * @param tile a GameTile
	 */
	public void addTile(final GameTile tile) {
		this.tile = tile;
		getChildren().add(this.tile);
	}
	
	/**
	 * Returns the GameTile object.
	 * @return GameTile
	 */
	public GameTile getTile() {
		return tile;
	}
	
	/**
	 * Returns true if there is only an empty tile child.
	 * @return boolean
	 */
	public boolean isEmpty() {
		return tile.getName().equals(LocalMessages.getString("EmptyTile"));
	}
	
	public void addMouseListener(final GameController controller,
			final EventHandler<MouseEvent> pressHandler) {
		removeMouseListener();
		this.pressHandler = pressHandler;
		if(pressHandler != null)
			addEventHandler(MouseEvent.MOUSE_PRESSED, this.pressHandler);
	}
	
	public void removeMouseListener() {
		if(pressHandler != null)
			removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
	}
	
	/**
	 * Returns the top segment of the tile.
	 * @return a Segment
	 */
	public Segment getTopSegment() {
		return tile.getTopSegment();
	}
	
	/**
	 * Returns the right segment of the tile.
	 * @return a Segment
	 */
	public Segment getRightSegment() {
		return tile.getRightSegment();
	}
	
	/**
	 * Returns the bottom segment of the tile.
	 * @return a Segment
	 */
	public Segment getBottomSegment() {
		return tile.getBottomSegment();
	}
	
	/**
	 * Returns the left segment of the tile.
	 * @return a Segment
	 */
	public Segment getLeftSegment() {
		return tile.getLeftSegment();
	}
	
	/**
	 * Returns segment at specified position.
	 * @param position the position
	 * @return the segment
	 */
	public Segment getSegment(final Position position) {
		return tile.getSegment(position);
	}
	
	/**
	 * Returns true if specified segment matches this tile's top segment.
	 * @param segment a segment
	 * @return a boolean
	 */
	public boolean matchesTopSegment(final Segment segment) {
		return tile.getTopSegment() == segment;
	}
	
	/**
	 * Returns true if specified segment matches this tile's right segment.
	 * @param segment a segment
	 * @return a boolean
	 */
	public boolean matchesRightSegment(final Segment segment) {
		return tile.getRightSegment() == segment;
	}
	
	/**
	 * Returns true if specified segment matches this tile's bottom segment.
	 * @param segment a segment
	 * @return a boolean
	 */
	public boolean matchesBottomSegment(final Segment segment) {
		return tile.getBottomSegment() == segment;
	}
	
	/**
	 * Returns true if specified segment matches this tile's left segment.
	 * @param segment a segment
	 * @return a boolean
	 */
	public boolean matchesLeftSegment(final Segment segment) {
		return tile.getLeftSegment() == segment;
	}
	
	/**
	 * Sets the TileContainer to be selected by graphical indication.
	 * @param state a boolean
	 * @param lastTile a TileContainer
	 */
	public abstract void setSelected(boolean state);
	
	/**
	 * Sets the style to the last placed style indicator.
	 */
	public void setLastPlacedTileStyle() {
		setStyle(latestStyle);
	}
}
