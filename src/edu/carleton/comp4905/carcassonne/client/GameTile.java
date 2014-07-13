package edu.carleton.comp4905.carcassonne.client;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView {
	protected String tileName;
	protected boolean hasShield;
	protected Map<Position, Segment> segments;
	protected Map<Position, String> positions;
	
	public GameTile() {
		super();
		segments = new EnumMap<Position, Segment>(Position.class);
		positions = new EnumMap<Position, String>(Position.class);
		hasShield = false;
	}
	
	public GameTile(final GameTile tile) {
		this();
		tileName = tile.tileName;
		hasShield = tile.hasShield;
		for(Position pos : tile.getSegments().keySet())
			segments.put(pos, tile.getSegments().get(pos));
		for(Position pos : tile.getPositions().keySet())
			positions.put(pos, tile.getPositions().get(pos));
		setImage(tile.getImage());
		setRotate(tile.getRotate());
	}
	
	public void setName(final String name) {
		this.tileName = name;
	}
	
	/**
	 * Sets the tile image and respective segments.
	 * @param name a String
	 * @param top a Segment
	 * @param right a Segment
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	public void setTile(final String name, final Map<Position, Segment> segments) {
		this.tileName = name;
		for(Position pos : segments.keySet())
			this.segments.put(pos, segments.get(pos));
		setImage(ResourceManager.getImageFromTiles(name));
	}
	
	/**
	 * Sets the respective segments.
	 * @param pos the position
	 * @param segment the segment
	 */
	public void setSegment(final Position pos, final Segment segment) {
		if(pos == null) return;
		segments.put(pos, segment);
	}
	
	/**
	 * Sets a possible follower placement to the tile.
	 * @param pos the position
	 * @param name the player name who owns the segment
	 */
	public void setPosition(final Position pos, final String name) {
		if(pos == null) return;
		positions.put(pos, name);
	}
	
	/**
	 * Swaps segment placements by 90 degrees.
	 */
	public void segmentsRotated90() {
		EnumMap<Position, Segment> temp = new EnumMap<Position, Segment>(segments);
		segments.clear();
		segments.put(Position.TOP, temp.get(Position.LEFT));
		segments.put(Position.RIGHT, temp.get(Position.TOP));
		segments.put(Position.BOTTOM, temp.get(Position.RIGHT));
		segments.put(Position.LEFT, temp.get(Position.BOTTOM));
		segments.put(Position.CENTER, temp.get(Position.CENTER));
		segments.put(Position.TOP_RIGHT, temp.get(Position.TOP_LEFT));
		segments.put(Position.BOTTOM_RIGHT, temp.get(Position.TOP_RIGHT));
		segments.put(Position.BOTTOM_LEFT, temp.get(Position.BOTTOM_RIGHT));
		segments.put(Position.TOP_LEFT, temp.get(Position.BOTTOM_LEFT));
		removeNullPositions(segments);
	}
	
	/**
	 * Swaps segment placements by 180 degrees.
	 */
	public void segmentsRotated180() {
		EnumMap<Position, Segment> temp = new EnumMap<Position, Segment>(segments);
		segments.clear();
		segments.put(Position.TOP, temp.get(Position.BOTTOM));
		segments.put(Position.RIGHT, temp.get(Position.LEFT));
		segments.put(Position.BOTTOM, temp.get(Position.TOP));
		segments.put(Position.LEFT, temp.get(Position.RIGHT));
		segments.put(Position.CENTER, temp.get(Position.CENTER));
		segments.put(Position.TOP_RIGHT, temp.get(Position.BOTTOM_LEFT));
		segments.put(Position.BOTTOM_RIGHT, temp.get(Position.TOP_LEFT));
		segments.put(Position.BOTTOM_LEFT, temp.get(Position.TOP_RIGHT));
		segments.put(Position.TOP_LEFT, temp.get(Position.BOTTOM_RIGHT));
		removeNullPositions(segments);
	}
	
	/**
	 * Swaps segment placements by 270 degrees.
	 */
	public void segmentsRotated270() {
		EnumMap<Position, Segment> temp = new EnumMap<Position, Segment>(segments);
		segments.clear();
		segments.put(Position.TOP, temp.get(Position.RIGHT));
		segments.put(Position.RIGHT, temp.get(Position.BOTTOM));
		segments.put(Position.BOTTOM, temp.get(Position.LEFT));
		segments.put(Position.LEFT, temp.get(Position.TOP));
		segments.put(Position.CENTER, temp.get(Position.CENTER));
		segments.put(Position.TOP_RIGHT, temp.get(Position.BOTTOM_RIGHT));
		segments.put(Position.BOTTOM_RIGHT, temp.get(Position.BOTTOM_LEFT));
		segments.put(Position.BOTTOM_LEFT, temp.get(Position.TOP_LEFT));
		segments.put(Position.TOP_LEFT, temp.get(Position.TOP_RIGHT));
		removeNullPositions(segments);
	}
	
	/**
	 * Swaps possible follower placements by 90 degrees.
	 */
	public void positionsRotated90() {
		EnumMap<Position, String> temp = new EnumMap<Position, String>(positions);
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
		removeNullPositions(positions);
	}
	
	/**
	 * Swaps possible follower placements by 180 degrees.
	 */
	public void positionsRotated180() {
		EnumMap<Position, String> temp = new EnumMap<Position, String>(positions);
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
		removeNullPositions(positions);
	}
	
	/**
	 * Swaps possible follower placements by 270 degrees.
	 */
	public void positionsRotated270() {
		EnumMap<Position, String> temp = new EnumMap<Position, String>(positions);
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
		removeNullPositions(positions);
	}
	
	/**
	 * Sets the positions all at once.
	 * @param positions the positions
	 */
	public void setPositions(final Map<Position, String> positions) {
		this.positions = positions;
	}
	
	/**
	 * Removes all null positions in map.
	 */
	public void removeNullPositions(final Map<Position, ?> map) {
		for(Position pos : map.keySet()) {
			if(map.get(pos) == null)
				map.remove(pos);
		}
	}
	
	/**
	 * Returns the name of the latest image file.
	 * @return a String
	 */
	public String getName() {
		return tileName;
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
	 * Returns segment at specified position.
	 * @param position the position
	 * @return the segment
	 */
	public Segment getSegment(final Position position) {
		return segments.get(position);
	}
	
	/**
	 * Returns a map of the positions.
	 * @return a map of the positions
	 */
	public Map<Position, String> getPositions() {
		return positions;
	}
	
	/**
	 * Returns the owner of the follower at specified position.
	 * @param position the position
	 * @return the player who placed the follower
	 */
	public String getFollowerOwner(final Position position) {
		return position == null ? null : positions.get(position);
	}
	
	/**
	 * Updates the connected segments.
	 * @param position the position
	 * @param name the player name
	 */
	public void updateSegmentOwners(final Position position, final String name) {
		Segment segment = segments.get(position);
		Set<Position> traversed = new HashSet<Position>();
		traversed.add(position);
		positions.put(position, name);
		updateSegmentOwners(segment, position, name, traversed);
	}
	
	/**
	 * Updates the segments owned by a player through similar adjacent segments.
	 * @param segment the segment
	 * @param current the current position
	 * @param name the player name
	 * @param traversed the list of traversed positions
	 */
	private void updateSegmentOwners(final Segment segment, final Position current, final String name, final Set<Position> traversed) {
		int hOffset = 1, vOffset = 3;
		int ordinal = current.ordinal();
		
		if((ordinal % 3) != 0) {
			Position newPos = Position.values()[Math.abs(ordinal - hOffset)];
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				positions.put(newPos, name);
				traversed.add(current);
				updateSegmentOwners(segment, newPos, name, traversed);
			}
		}
		if((ordinal % 3) != 2) {
			Position newPos = Position.values()[ordinal + hOffset];
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				traversed.add(current);
				positions.put(newPos, name);
				updateSegmentOwners(segment, newPos, name, traversed);
			}
		}
		if(ordinal >= 3) {
			Position newPos = Position.values()[Math.abs(ordinal - vOffset)];
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				positions.put(newPos, name);
				traversed.add(current);
				updateSegmentOwners(segment, newPos, name, traversed);
			}
		}
		if(ordinal <= 5) {
			Position newPos = Position.values()[ordinal + vOffset];
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				positions.put(newPos, name);
				traversed.add(current);
				updateSegmentOwners(segment, newPos, name, traversed);
			}
		}
	}
	
	/**
	 * Removes the segment information at specified position.
	 * @param position the position
	 */
	public void removeSegment(final Position position) {
		segments.remove(position);
		positions.remove(position);		
	}
	
	/**
	 * Sets whether there is a shield on this tile.
	 * @param state the state
	 */
	public void setShield(final boolean state) {
		hasShield = state;
	}
	
	/**
	 * Returns whether this tile has a shield.
	 */
	public boolean hasShield() {
		return hasShield;
	}
	
	/**
	 * Rotates the tile based on the provided degrees.
	 * @param degrees the degrees
	 */
	public void rotate(final double degrees) {
		if(degrees == 90) {
			positionsRotated90();
			segmentsRotated90();
		}
		else if(degrees == 180) {
			positionsRotated180();
			segmentsRotated180();
		}
		else if(degrees == 270) {
			positionsRotated270();
			segmentsRotated270();
		}
		setRotate(degrees);
	}
}
