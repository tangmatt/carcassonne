package edu.carleton.comp4905.carcassonne.client;

import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.StringConstants;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class TileBase extends StackPane {
	private GameTile tile;
	private EventHandler<MouseEvent> handler;
	public static final String selectedStyle = "-fx-border-color: #2F8BFA;"
            + "-fx-border-width: 2;"
			+ "-fx-border-insets: -1;"
            + "-fx-border-style: solid;";
	
	public TileBase(GameTile tile) {
		addTile(tile);
		setSelected(false);
	}
	
	/**
	 * Adds a new GameTile above the existing GameTile(s).
	 * @param tile a GameTile
	 */
	public void addTile(GameTile tile) {
		this.tile = tile;
		getChildren().add(tile);
		setSelected(false);
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
	
	public void addMouseListener(GameController controller, EventHandler<MouseEvent> handler) {
		this.handler = handler;
		addEventHandler(MouseEvent.MOUSE_PRESSED, handler);
		//setCursor(Cursor.HAND);
	}
	
	public void removePreviewSelectListener() {
		removeEventHandler(MouseEvent.MOUSE_PRESSED, handler);
		//setCursor(Cursor.DEFAULT);
	}
	
	public Segment getTopSegment() {
		return tile.getTopSegment();
	}
	
	public Segment getRightSegment() {
		return tile.getRightSegment();
	}
	
	public Segment getBottomSegment() {
		return tile.getBottomSegment();
	}
	
	public Segment getLeftSegment() {
		return tile.getLeftSegment();
	}
	
	public boolean matchesTopSegment(Segment segment) {
		return tile.getTopSegment() == segment;
	}
	
	public boolean matchesRightSegment(Segment segment) {
		return tile.getRightSegment() == segment;
	}
	
	public boolean matchesBottomSegment(Segment segment) {
		return tile.getBottomSegment() == segment;
	}
	
	public boolean matchesLeftSegment(Segment segment) {
		return tile.getLeftSegment() == segment;
	}
	
	/**
	 * Sets the TileContainer to be selected by graphical indication.
	 * @param state a boolean
	 */
	public abstract void setSelected(boolean state);
}
