package edu.carleton.comp4905.carcassonne.client;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView implements Cloneable {
	private Segment top, right, bottom, left;
	private String name;
	private Map<Position, Boolean> positions;
	
	public GameTile() {
		super();
		positions = new EnumMap<Position, Boolean>(Position.class);
	}
	
	public GameTile(final GameTile tile) {
		this.name = tile.getName();
		setTile(tile);
		positions = new EnumMap<Position, Boolean>(tile.getPositions());
		rotate(tile.getRotate());
	}
	
	/**
	 * Sets the tile image and respective segments.
	 * @param tile a GameTile
	 */
	private void setTile(GameTile tile) {
		setSegments(tile.top, tile.right, tile.bottom, tile.left);
		setImage(tile.getImage());
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
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
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
	 * Returns a set of segments.
	 * @return Set<Segment>
	 */
	public Set<Segment> getSegments() {
		return EnumSet.of(top, right, bottom, left);
	}
	
	/**
	 * Returns the top segment of the tile.
	 * @return a Segment
	 */
	public Segment getTopSegment() {
		return top;
	}
	
	/**
	 * Returns the right segment of the tile.
	 * @return a Segment
	 */
	public Segment getRightSegment() {
		return right;
	}
	
	/**
	 * Returns the bottom segment of the tile.
	 * @return a Segment
	 */
	public Segment getBottomSegment() {
		return bottom;
	}
	
	/**
	 * Returns the left segment of the tile.
	 * @return a Segment
	 */
	public Segment getLeftSegment() {
		return left;
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
		if(degrees == 90) {
			setSegments(left, top, right, bottom);
			positionsRotated90();
		}
		else if(degrees == 180) {
			setSegments(bottom, left, top, right);
			positionsRotated180();
		}
		else if(degrees == 270) {
			setSegments(right, bottom, left, top);
			positionsRotated270();
		}
		setRotate(degrees);
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
