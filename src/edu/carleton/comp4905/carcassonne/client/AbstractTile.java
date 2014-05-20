package edu.carleton.comp4905.carcassonne.client;

import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.StringConstants;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class AbstractTile extends StackPane {
	protected GameTile tile;
	protected EventHandler<MouseEvent> handler;
	protected boolean state;
	public static final String selectedStyle = "-fx-border-color: #2F8BFA;"
            + "-fx-border-width: 2;"
			+ "-fx-border-insets: -1;"
            + "-fx-border-style: solid;";
	
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
		setSelected(state);
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
		if(getChildren().size() == 0)
			return true;
		if(getChildren().size() > 1)
			return false;
		GameTile child = (GameTile)getChildren().get(0);
		return child.getName().equals(StringConstants.EMPTY_TILE);
	}
	
	/**
	 * Returns a set of Segments.
	 * @return Set<Segment>
	 */
	public Set<Segment> getTileSegments() {
		return tile.getSegments();
	}
	
	public void addMouseListener(final GameController controller, final EventHandler<MouseEvent> handler) {
		removeMouseListener();
		this.handler = handler;
		addEventHandler(MouseEvent.MOUSE_PRESSED, this.handler);
	}
	
	public void removeMouseListener() {
		if(handler == null)
			return;
		removeEventHandler(MouseEvent.MOUSE_PRESSED, handler);
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
	 */
	public abstract void setSelected(boolean state);
}
