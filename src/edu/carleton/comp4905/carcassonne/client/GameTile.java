package edu.carleton.comp4905.carcassonne.client;

import java.util.EnumMap;
import java.util.Map;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView {
	private String name;
	private Map<Position, Segment> segments;
	private Map<Position, Boolean> positions;
	
	public GameTile() {
		super();
		segments = new EnumMap<Position, Segment>(Position.class);
		positions = new EnumMap<Position, Boolean>(Position.class);
	}
	
	public GameTile(final GameTile tile) {
		this();
		name = tile.getName();
		/*setTile(tile);
		positions = tile.getPositions();*/
		for(Position pos : tile.getSegments().keySet())
			segments.put(pos, tile.getSegments().get(pos));
		for(Position pos : tile.getPositions().keySet())
			positions.put(pos, tile.getPositions().get(pos));
		setImage(tile.getImage());
		setRotate(tile.getRotate());
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Sets the tile image and respective segments.
	 * @param name a String
	 * @param top a Segment
	 * @param right a Segment
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	public void setTile(final String name, final Segment top, final Segment right, final Segment bottom, final Segment left) {
		this.name = name;
		setSegments(top, right, bottom, left);
		setImage(ResourceManager.getImageFromTiles(name));
	}
	
	/**
	 * Sets the respective segments.
	 * @param top a Segment
	 * @param right a Segment
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	public void setSegments(final Segment top, final Segment right, final Segment bottom, final Segment left) {
		segments.put(Position.TOP, top);
		segments.put(Position.RIGHT, right);
		segments.put(Position.BOTTOM, bottom);
		segments.put(Position.LEFT, left);
	}
	
	/**
	 * Adds a possible follower placement to the tile.
	 * @param pos the position
	 */
	public void addPosition(final Position pos) {
		positions.put(pos, true);
	}
	
	/**
	 * Swaps possible follower placements by 90 degrees.
	 */
	public void positionsRotated90() {
		EnumMap<Position, Boolean> temp = new EnumMap<Position, Boolean>(positions);
		positions.clear();
		positions.put(Position.TOP, temp.get(Position.RIGHT));
		positions.put(Position.RIGHT, temp.get(Position.BOTTOM));
		positions.put(Position.BOTTOM, temp.get(Position.LEFT));
		positions.put(Position.LEFT, temp.get(Position.TOP));
		positions.put(Position.CENTER, temp.get(Position.CENTER));
		positions.put(Position.TOP_RIGHT, temp.get(Position.BOTTOM_RIGHT));
		positions.put(Position.BOTTOM_RIGHT, temp.get(Position.BOTTOM_LEFT));
		positions.put(Position.BOTTOM_LEFT, temp.get(Position.TOP_LEFT));
		positions.put(Position.TOP_LEFT, temp.get(Position.TOP_RIGHT));
		removeNullPositions();
	}
	
	/**
	 * Swaps possible follower placements by 180 degrees.
	 */
	public void positionsRotated180() {
		EnumMap<Position, Boolean> temp = new EnumMap<Position, Boolean>(positions);
		positions.clear();
		positions.put(Position.TOP, temp.get(Position.BOTTOM));
		positions.put(Position.RIGHT, temp.get(Position.LEFT));
		positions.put(Position.BOTTOM, temp.get(Position.TOP));
		positions.put(Position.LEFT, temp.get(Position.RIGHT));
		positions.put(Position.CENTER, temp.get(Position.CENTER));
		positions.put(Position.TOP_RIGHT, temp.get(Position.BOTTOM_LEFT));
		positions.put(Position.BOTTOM_RIGHT, temp.get(Position.TOP_LEFT));
		positions.put(Position.BOTTOM_LEFT, temp.get(Position.TOP_RIGHT));
		positions.put(Position.TOP_LEFT, temp.get(Position.BOTTOM_RIGHT));
		removeNullPositions();
	}
	
	/**
	 * Swaps possible follower placements by 270 degrees.
	 */
	public void positionsRotated270() {
		EnumMap<Position, Boolean> temp = new EnumMap<Position, Boolean>(positions);
		positions.clear();
		positions.put(Position.TOP, temp.get(Position.LEFT));
		positions.put(Position.RIGHT, temp.get(Position.TOP));
		positions.put(Position.BOTTOM, temp.get(Position.RIGHT));
		positions.put(Position.LEFT, temp.get(Position.BOTTOM));
		positions.put(Position.CENTER, temp.get(Position.CENTER));
		positions.put(Position.TOP_RIGHT, temp.get(Position.TOP_LEFT));
		positions.put(Position.BOTTOM_RIGHT, temp.get(Position.TOP_RIGHT));
		positions.put(Position.BOTTOM_LEFT, temp.get(Position.BOTTOM_RIGHT));
		positions.put(Position.TOP_LEFT, temp.get(Position.BOTTOM_LEFT));
		removeNullPositions();
	}
	
	/**
	 * Sets the positions all at once.
	 * @param positions the positions
	 */
	public void setPositions(final Map<Position, Boolean> positions) {
		this.positions = positions;
	}
	
	/**
	 * Removes all null positions in map.
	 */
	public void removeNullPositions() {
		for(Position pos : positions.keySet()) {
			if(positions.get(pos) == null)
				positions.remove(pos);
		}
	}
	
	/**
	 * Returns the name of the latest image file.
	 * @return a String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the segments.
	 * @return the segments
	 */
	public Map<Position, Segment> getSegments() {
		return segments;
	}
	
	/**
	 * Returns the top segment of the tile.
	 * @return a Segment
	 */
	public Segment getTopSegment() {
		return segments.get(Position.TOP);
	}
	
	/**
	 * Returns the right segment of the tile.
	 * @return a Segment
	 */
	public Segment getRightSegment() {
		return segments.get(Position.RIGHT);
	}
	
	/**
	 * Returns the bottom segment of the tile.
	 * @return a Segment
	 */
	public Segment getBottomSegment() {
		return segments.get(Position.BOTTOM);
	}
	
	/**
	 * Returns the left segment of the tile.
	 * @return a Segment
	 */
	public Segment getLeftSegment() {
		return segments.get(Position.LEFT);
	}
	
	/**
	 * Returns a map of the positions.
	 * @return a map of the positions
	 */
	public Map<Position, Boolean> getPositions() {
		return positions;
	}
	
	/**
	 * Rotates the tile based on the provided degrees.
	 * @param degrees the degrees
	 */
	public void rotate(final double degrees) {
		Segment left = getLeftSegment(), top = getTopSegment(), right = getRightSegment(), bottom = getBottomSegment();
		if(degrees == 90) {
			positionsRotated90();
			setSegments(left, top, right, bottom);
		}
		else if(degrees == 180) {
			positionsRotated180();
			setSegments(bottom, left, top, right);
		}
		else if(degrees == 270) {
			positionsRotated270();
			setSegments(right, bottom, left, top);
		}
		setRotate(degrees);
	}
}
