package edu.carleton.comp4905.carcassonne.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView {
	protected String tileName;
	protected Position shield;
	protected Map<Position, Segment> segments;
	protected Map<Position, PositionData> positions;
	protected Set<Position> dividers;
	
	public GameTile() {
		super();
		segments = new EnumMap<Position, Segment>(Position.class);
		positions = new HashMap<Position, PositionData>();
		dividers = new HashSet<Position>();
	}
	
	public GameTile(final GameTile tile) {
		this();
		tileName = tile.tileName;
		shield = tile.shield;
		for(Position pos : tile.getSegments().keySet())
			segments.put(pos, tile.getSegments().get(pos));
		for(Position pos : tile.getPositions().keySet())
			addPosition(pos, tile.getPositions().get(pos));
		for(Position pos : tile.getDividers())
			dividers.add(pos);
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
		positions.put(pos, new PositionData(name));
	}
	
	/**
	 * Maps the position as a divider.
	 * @param pos the position
	 */
	public void addDividedArea(final Position pos) {
		if(pos == null) return;
		dividers.add(pos);
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
		segments.put(Position.TOP_RIGHT_RIGHT, temp.get(Position.TOP_LEFT_TOP));
		segments.put(Position.TOP_RIGHT_TOP, temp.get(Position.TOP_LEFT_LEFT));
		segments.put(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.TOP_RIGHT_TOP));
		segments.put(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.TOP_RIGHT_RIGHT));
		segments.put(Position.BOTTOM_LEFT_LEFT, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		segments.put(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		segments.put(Position.TOP_LEFT_LEFT, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		segments.put(Position.TOP_LEFT_TOP, temp.get(Position.BOTTOM_LEFT_LEFT));
		removeNullData(segments);
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
		segments.put(Position.TOP_RIGHT_RIGHT, temp.get(Position.BOTTOM_LEFT_LEFT));
		segments.put(Position.TOP_RIGHT_TOP, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		segments.put(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.TOP_LEFT_LEFT));
		segments.put(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.TOP_LEFT_TOP));
		segments.put(Position.BOTTOM_LEFT_LEFT, temp.get(Position.TOP_RIGHT_RIGHT));
		segments.put(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.TOP_RIGHT_TOP));
		segments.put(Position.TOP_LEFT_LEFT, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		segments.put(Position.TOP_LEFT_TOP, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		removeNullData(segments);
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
		segments.put(Position.TOP_RIGHT_RIGHT, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		segments.put(Position.TOP_RIGHT_TOP, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		segments.put(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		segments.put(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.BOTTOM_LEFT_LEFT));
		segments.put(Position.BOTTOM_LEFT_LEFT, temp.get(Position.TOP_LEFT_TOP));
		segments.put(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.TOP_LEFT_LEFT));
		segments.put(Position.TOP_LEFT_LEFT, temp.get(Position.TOP_RIGHT_TOP));
		segments.put(Position.TOP_LEFT_TOP, temp.get(Position.TOP_RIGHT_RIGHT));
		removeNullData(segments);
	}
	
	/**
	 * Swaps possible follower placements by 90 degrees.
	 */
	public void positionsRotated90() {
		HashMap<Position, PositionData> temp = new HashMap<Position, PositionData>(positions);
		Set<Position> tempDividers = new HashSet<Position>(dividers);
		positions.clear();
		dividers.clear();
		addPosition(Position.TOP, temp.get(Position.LEFT));
		addPosition(Position.RIGHT, temp.get(Position.TOP));
		addPosition(Position.BOTTOM, temp.get(Position.RIGHT));
		addPosition(Position.LEFT, temp.get(Position.BOTTOM));
		addPosition(Position.CENTER, temp.get(Position.CENTER));
		addPosition(Position.TOP_RIGHT_RIGHT, temp.get(Position.TOP_LEFT_TOP));
		addPosition(Position.TOP_RIGHT_TOP, temp.get(Position.TOP_LEFT_LEFT));
		addPosition(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.TOP_RIGHT_TOP));
		addPosition(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.TOP_RIGHT_RIGHT));
		addPosition(Position.BOTTOM_LEFT_LEFT, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		addPosition(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		addPosition(Position.TOP_LEFT_LEFT, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		addPosition(Position.TOP_LEFT_TOP, temp.get(Position.BOTTOM_LEFT_LEFT));
		for(Position pos : tempDividers) {
			if(pos == Position.TOP_RIGHT)
				dividers.add(Position.BOTTOM_RIGHT);
			else if(pos == Position.BOTTOM_RIGHT)
				dividers.add(Position.BOTTOM_LEFT);
			else if(pos == Position.TOP_LEFT)
				dividers.add(Position.TOP_RIGHT);
			else if(pos == Position.BOTTOM_LEFT)
				dividers.add(Position.TOP_LEFT);
		}
		removeNullData(positions);
	}
	
	/**
	 * Swaps possible follower placements by 180 degrees.
	 */
	public void positionsRotated180() {
		HashMap<Position, PositionData> temp = new HashMap<Position, PositionData>(positions);
		Set<Position> tempDividers = new HashSet<Position>(dividers);
		positions.clear();
		dividers.clear();
		addPosition(Position.TOP, temp.get(Position.BOTTOM));
		addPosition(Position.RIGHT, temp.get(Position.LEFT));
		addPosition(Position.BOTTOM, temp.get(Position.TOP));
		addPosition(Position.LEFT, temp.get(Position.RIGHT));
		addPosition(Position.CENTER, temp.get(Position.CENTER));
		addPosition(Position.TOP_RIGHT_RIGHT, temp.get(Position.BOTTOM_LEFT_LEFT));
		addPosition(Position.TOP_RIGHT_TOP, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		addPosition(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.TOP_LEFT_LEFT));
		addPosition(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.TOP_LEFT_TOP));
		addPosition(Position.BOTTOM_LEFT_LEFT, temp.get(Position.TOP_RIGHT_RIGHT));
		addPosition(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.TOP_RIGHT_TOP));
		addPosition(Position.TOP_LEFT_LEFT, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		addPosition(Position.TOP_LEFT_TOP, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		for(Position pos : tempDividers) {
			if(pos == Position.TOP_RIGHT)
				dividers.add(Position.BOTTOM_LEFT);
			else if(pos == Position.BOTTOM_RIGHT)
				dividers.add(Position.TOP_LEFT);
			else if(pos == Position.TOP_LEFT)
				dividers.add(Position.BOTTOM_RIGHT);
			else if(pos == Position.BOTTOM_LEFT)
				dividers.add(Position.TOP_RIGHT);
		}
		removeNullData(positions);
	}
	
	/**
	 * Swaps possible follower placements by 270 degrees.
	 */
	public void positionsRotated270() {
		HashMap<Position, PositionData> temp = new HashMap<Position, PositionData>(positions);
		Set<Position> tempDividers = new HashSet<Position>(dividers);
		positions.clear();
		dividers.clear();
		addPosition(Position.TOP, temp.get(Position.RIGHT));
		addPosition(Position.RIGHT, temp.get(Position.BOTTOM));
		addPosition(Position.BOTTOM, temp.get(Position.LEFT));
		addPosition(Position.LEFT, temp.get(Position.TOP));
		addPosition(Position.CENTER, temp.get(Position.CENTER));
		addPosition(Position.TOP_RIGHT_RIGHT, temp.get(Position.BOTTOM_RIGHT_BOTTOM));
		addPosition(Position.TOP_RIGHT_TOP, temp.get(Position.BOTTOM_RIGHT_RIGHT));
		addPosition(Position.BOTTOM_RIGHT_RIGHT, temp.get(Position.BOTTOM_LEFT_BOTTOM));
		addPosition(Position.BOTTOM_RIGHT_BOTTOM, temp.get(Position.BOTTOM_LEFT_LEFT));
		addPosition(Position.BOTTOM_LEFT_LEFT, temp.get(Position.TOP_LEFT_TOP));
		addPosition(Position.BOTTOM_LEFT_BOTTOM, temp.get(Position.TOP_LEFT_LEFT));
		addPosition(Position.TOP_LEFT_LEFT, temp.get(Position.TOP_RIGHT_TOP));
		addPosition(Position.TOP_LEFT_TOP, temp.get(Position.TOP_RIGHT_RIGHT));
		for(Position pos : tempDividers) {
			if(pos == Position.TOP_RIGHT)
				dividers.add(Position.TOP_LEFT);
			else if(pos == Position.BOTTOM_RIGHT)
				dividers.add(Position.TOP_RIGHT);
			else if(pos == Position.TOP_LEFT)
				dividers.add(Position.BOTTOM_LEFT);
			else if(pos == Position.BOTTOM_LEFT)
				dividers.add(Position.BOTTOM_RIGHT);
		}
		removeNullData(positions);
	}
	
	/**
	 * Swaps segment placements by 90 degrees.
	 */
	public void shieldsRotated90() {
		if(shield == Position.TOP)
			shield = Position.LEFT;
		else if(shield == Position.RIGHT)
			shield = Position.TOP;
		else if(shield == Position.BOTTOM)
			shield = Position.RIGHT;
		else if(shield == Position.LEFT)
			shield = Position.BOTTOM;
		else if(shield == Position.CENTER)
			shield = Position.CENTER;
		else if(shield == Position.TOP_RIGHT_RIGHT)
			shield = Position.TOP_LEFT_TOP;
		else if(shield == Position.TOP_RIGHT_TOP)
			shield = Position.TOP_LEFT_LEFT;
		else if(shield == Position.BOTTOM_RIGHT_RIGHT)
			shield = Position.TOP_RIGHT_TOP;
		else if(shield == Position.BOTTOM_RIGHT_BOTTOM)
			shield = Position.TOP_RIGHT_RIGHT;
		else if(shield == Position.BOTTOM_LEFT_LEFT)
			shield = Position.BOTTOM_RIGHT_BOTTOM;
		else if(shield == Position.BOTTOM_LEFT_BOTTOM)
			shield = Position.BOTTOM_RIGHT_RIGHT;
		else if(shield == Position.TOP_LEFT_LEFT)
			shield = Position.BOTTOM_LEFT_BOTTOM;
		else if(shield == Position.TOP_LEFT_TOP)
			shield = Position.BOTTOM_LEFT_LEFT;
	}
	
	/**
	 * Swaps segment placements by 180 degrees.
	 */
	public void shieldsRotated180() {
		if(shield == Position.TOP)
			shield = Position.BOTTOM;
		else if(shield == Position.RIGHT)
			shield = Position.LEFT;
		else if(shield == Position.BOTTOM)
			shield = Position.TOP;
		else if(shield == Position.LEFT)
			shield = Position.RIGHT;
		else if(shield == Position.CENTER)
			shield = Position.CENTER;
		else if(shield == Position.TOP_RIGHT_RIGHT)
			shield = Position.BOTTOM_LEFT_LEFT;
		else if(shield == Position.TOP_RIGHT_TOP)
			shield = Position.BOTTOM_LEFT_BOTTOM;
		else if(shield == Position.BOTTOM_RIGHT_RIGHT)
			shield = Position.TOP_LEFT_LEFT;
		else if(shield == Position.BOTTOM_RIGHT_BOTTOM)
			shield = Position.TOP_LEFT_TOP;
		else if(shield == Position.BOTTOM_LEFT_LEFT)
			shield = Position.TOP_RIGHT_RIGHT;
		else if(shield == Position.BOTTOM_LEFT_BOTTOM)
			shield = Position.TOP_RIGHT_TOP;
		else if(shield == Position.TOP_LEFT_LEFT)
			shield = Position.BOTTOM_RIGHT_RIGHT;
		else if(shield == Position.TOP_LEFT_TOP)
			shield = Position.BOTTOM_RIGHT_BOTTOM;
	}
	
	/**
	 * Swaps segment placements by 270 degrees.
	 */
	public void shieldsRotated270() {
		if(shield == Position.TOP)
			shield = Position.RIGHT;
		else if(shield == Position.RIGHT)
			shield = Position.BOTTOM;
		else if(shield == Position.BOTTOM)
			shield = Position.LEFT;
		else if(shield == Position.LEFT)
			shield = Position.TOP;
		else if(shield == Position.CENTER)
			shield = Position.CENTER;
		else if(shield == Position.TOP_RIGHT_RIGHT)
			shield = Position.BOTTOM_RIGHT_BOTTOM;
		else if(shield == Position.TOP_RIGHT_TOP)
			shield = Position.BOTTOM_RIGHT_RIGHT;
		else if(shield == Position.BOTTOM_RIGHT_RIGHT)
			shield = Position.BOTTOM_LEFT_BOTTOM;
		else if(shield == Position.BOTTOM_RIGHT_BOTTOM)
			shield = Position.BOTTOM_LEFT_LEFT;
		else if(shield == Position.BOTTOM_LEFT_LEFT)
			shield = Position.TOP_LEFT_TOP;
		else if(shield == Position.BOTTOM_LEFT_BOTTOM)
			shield = Position.TOP_LEFT_LEFT;
		else if(shield == Position.TOP_LEFT_LEFT)
			shield = Position.TOP_RIGHT_TOP;
		else if(shield == Position.TOP_LEFT_TOP)
			shield = Position.TOP_RIGHT_RIGHT;
	}
	
	/**
	 * Records the position owner.
	 * @param position the position
	 * @param name the player name
	 * @param id the identifier
	 */
	public void addPosition(final Position position, final String name, final int id) {
		addPosition(position, new PositionData(name, id));
	}
	
	/**
	 * Records the position owner
	 * @param position the position
	 * @param data the position data
	 */
	public void addPosition(final Position position, final PositionData data) {
		positions.put(position, data);
	}
	
	/**
	 * Sets the positions all at once.
	 * @param positions the positions
	 */
	public void setPositions(final Map<Position, PositionData> positions) {
		this.positions = positions;
	}
	
	/**
	 * Removes all null positions in map.
	 * @param map a map
	 */
	@SuppressWarnings("rawtypes")
	public void removeNullData(final Map<Position, ?> map) {
		Iterator<?> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			if(pairs.getValue() == null ||
					(pairs.getValue() instanceof PositionData && ((PositionData)pairs.getValue()).getId() == -1)) {
				it.remove();
			}
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
	public Map<Position, PositionData> getPositions() {
		return positions;
	}
	
	/**
	 * Returns the owner at specified position.
	 * @param position the position
	 * @return the player who placed the follower
	 */
	public String getPositionOwner(final Position position) {
		return position == null || positions.get(position) == null ? null : positions.get(position).getName();
	}
	
	/**
	 * Returns the id at specified position.
	 * @param position the position
	 * @return the id of the connected segment
	 */
	public int getPositionId(final Position position) {
		return position == null || positions.get(position) == null ? PositionData.INVALID_ID : positions.get(position).getId();
	}
	
	/**
	 * Returns the position data
	 * @param position the position
	 */
	public PositionData getPositionData(final Position position) {
		return position == null || positions.get(position) == null ? null : positions.get(position);
	}
	
	/**
	 * Updates the connected segments.
	 * @param position the position
	 * @param name the player name
	 */
	public Set<Position> updateSegmentOwners(final Position position, String name, final int id) {
		Segment segment = segments.get(position);
		Set<Position> traversed = new HashSet<Position>();
		Set<Position> outerPositions = new HashSet<Position>();
		if(name == null)
			name = "";
		addPosition(position, name, id);
		traversed.add(position);
		updateSegmentOwners(segment, position, name, traversed, outerPositions, id);
	    return outerPositions;
	}
	
	/**
	 * Updates the segments owned by a player through similar adjacent segments.
	 * @param segment the segment
	 * @param current the current position
	 * @param name the player name
	 * @param traversed the list of traversed positions
	 * @param outerPositions the list of traversed positions on the outer positions
	 */
	private void updateSegmentOwners(final Segment segment, final Position current, String name, final Set<Position> traversed,
			final Set<Position> outerPositions, final int id) {
		
		if(name == null)
			name = "";
		
		// check corners
		List<Position> corners = new ArrayList<Position>(Arrays.asList(new Position[]{Position.TOP_LEFT_LEFT, Position.TOP_RIGHT_RIGHT, Position.BOTTOM_LEFT_LEFT, Position.BOTTOM_RIGHT_RIGHT,
				Position.TOP_LEFT_TOP, Position.TOP_RIGHT_TOP, Position.BOTTOM_LEFT_BOTTOM, Position.BOTTOM_RIGHT_BOTTOM}));
		
		if(current == Position.BOTTOM_RIGHT_BOTTOM) {
			Position newPos = Position.BOTTOM_RIGHT_RIGHT;
			if(segment == segments.get(newPos) && !isDivided(Position.BOTTOM_RIGHT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_RIGHT_TOP) {
			Position newPos = Position.TOP_RIGHT_RIGHT;
			if(segment == segments.get(newPos) && !isDivided(Position.TOP_RIGHT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_LEFT_LEFT) {
			Position newPos = Position.TOP_LEFT_TOP;
			if(segment == segments.get(newPos) && !isDivided(Position.TOP_LEFT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_LEFT_LEFT) {
			Position newPos = Position.BOTTOM_LEFT_BOTTOM;
			if(segment == segments.get(newPos) && !isDivided(Position.BOTTOM_LEFT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_RIGHT_RIGHT) {
			Position newPos = Position.BOTTOM_RIGHT_BOTTOM;
			if(segment == segments.get(newPos) && !isDivided(Position.BOTTOM_RIGHT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_RIGHT_RIGHT) {
			Position newPos = Position.TOP_RIGHT_TOP;
			if(segment == segments.get(newPos) && !isDivided(Position.TOP_RIGHT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_LEFT_BOTTOM) {
			Position newPos = Position.BOTTOM_LEFT_LEFT;
			if(segment == segments.get(newPos) && !isDivided(Position.BOTTOM_LEFT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}
		}
		
		if(current == Position.TOP_LEFT_TOP) {
			Position newPos = Position.TOP_LEFT_LEFT;
			if(segment == segments.get(newPos) && !isDivided(Position.TOP_LEFT) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		// exceptions with diagonal paths (for roads)
		if(current == Position.LEFT) {
			Position[] listOfPos = new Position[]{Position.BOTTOM, Position.TOP};
			for(Position newPos: listOfPos) {
				if(segment == segments.get(newPos) && segments.get(newPos) == Segment.ROAD && !traversed.contains(newPos)
						&& segments.get(Position.CENTER) == Segment.FIELD) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				}		
			}
		}
		
		if(current == Position.TOP) {
			Position[] listOfPos = new Position[]{Position.RIGHT, Position.LEFT};
			for(Position newPos: listOfPos) {
				if(segment == segments.get(newPos) && segments.get(newPos) == Segment.ROAD && !traversed.contains(newPos)
						&& segments.get(Position.CENTER) == Segment.FIELD) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				}		
			}
		}
		
		if(current == Position.BOTTOM) {
			Position[] listOfPos = new Position[]{Position.RIGHT, Position.LEFT};
			for(Position newPos: listOfPos) {
				if(segment == segments.get(newPos) && segments.get(newPos) == Segment.ROAD && !traversed.contains(newPos)
						&& segments.get(Position.CENTER) == Segment.FIELD) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				}		
			}
		}
		
		if(current == Position.RIGHT) {
			Position[] listOfPos = new Position[]{Position.BOTTOM, Position.TOP};
			for(Position newPos: listOfPos) {
				if(segment == segments.get(newPos) && segments.get(newPos) == Segment.ROAD && !traversed.contains(newPos)
						&& segments.get(Position.CENTER) == Segment.FIELD) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				}		
			}
		}
		
		// check left
		if(current == Position.CENTER) {
			Position newPos = Position.LEFT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.RIGHT) {
			Position newPos = Position.CENTER;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP) {
			Position newPos = Position.TOP_LEFT_TOP;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_RIGHT_TOP) {
			Position newPos = Position.TOP;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM) {
			Position newPos = Position.BOTTOM_LEFT_BOTTOM;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_RIGHT_BOTTOM) {
			Position newPos = Position.BOTTOM;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		// check right
		if(current == Position.CENTER) {
			Position newPos = Position.RIGHT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.LEFT) {
			Position newPos = Position.CENTER;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP) {
			Position newPos = Position.TOP_RIGHT_TOP;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_LEFT_TOP) {
			Position newPos = Position.TOP;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM) {
			Position newPos = Position.BOTTOM_RIGHT_BOTTOM;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_LEFT_BOTTOM) {
			Position newPos = Position.BOTTOM;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		// check top
		if(current == Position.CENTER) {
			Position newPos = Position.TOP;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.LEFT) {
			Position newPos = Position.TOP_LEFT_LEFT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.RIGHT) {
			Position newPos = Position.TOP_RIGHT_RIGHT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_LEFT_LEFT) {
			Position newPos = Position.LEFT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM_RIGHT_RIGHT) {
			Position newPos = Position.RIGHT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.BOTTOM) {
			Position newPos = Position.CENTER;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		// check bottom
		if(current == Position.CENTER) {
			Position newPos = Position.BOTTOM;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.LEFT) {
			Position newPos = Position.BOTTOM_LEFT_LEFT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.RIGHT) {
			Position newPos = Position.BOTTOM_RIGHT_RIGHT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_LEFT_LEFT) {
			Position newPos = Position.LEFT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP_RIGHT_RIGHT) {
			Position newPos = Position.RIGHT;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		if(current == Position.TOP) {
			Position newPos = Position.CENTER;
			if(segment == segments.get(newPos) && !traversed.contains(newPos)) {
				updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
			}	
		}
		
		// special cases
		if(corners.contains(current)) {
			Position newPos = Position.CENTER;
			// for tiles with road from bottom to right or top to left, etc.
			if(segment == segments.get(newPos) && segments.get(newPos) == Segment.FIELD && !traversed.contains(newPos)) {
				if(((current == Position.BOTTOM_LEFT_LEFT || current == Position.BOTTOM_LEFT_BOTTOM) && (segments.get(Position.LEFT) != Segment.FIELD && segments.get(Position.BOTTOM) != Segment.FIELD)) ||
						((current == Position.BOTTOM_RIGHT_RIGHT || current == Position.BOTTOM_RIGHT_BOTTOM) && segments.get(Position.RIGHT) == Segment.FIELD && segments.get(Position.BOTTOM) != Segment.FIELD) ||
						((current == Position.TOP_LEFT_LEFT || current == Position.TOP_LEFT_TOP) && segments.get(Position.LEFT) == Segment.FIELD && segments.get(Position.TOP) != Segment.FIELD) ||
						((current == Position.TOP_RIGHT_RIGHT || current == Position.TOP_RIGHT_TOP) && segments.get(Position.RIGHT) == Segment.FIELD && segments.get(Position.TOP) != Segment.FIELD)) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				} // for tile10,11 [may need to be improved/accommodate if more tiles come in]
				else if((current == Position.BOTTOM_LEFT_LEFT && (segment == segments.get(Position.LEFT) || segment == segments.get(Position.TOP_RIGHT_RIGHT))) ||
						(current == Position.TOP_LEFT_LEFT && (segment == segments.get(Position.LEFT) || segment == segments.get(Position.BOTTOM_RIGHT_BOTTOM))) ||
						(current == Position.TOP_LEFT_TOP && (segment == segments.get(Position.TOP) || segment == segments.get(Position.BOTTOM_RIGHT_RIGHT))) ||
						(current == Position.TOP_RIGHT_TOP && (segment == segments.get(Position.TOP) || segment == segments.get(Position.BOTTOM_LEFT_LEFT))) ||
						(current == Position.TOP_RIGHT_RIGHT && (segment == segments.get(Position.RIGHT) || segment == segments.get(Position.BOTTOM_LEFT_BOTTOM))) ||
						(current == Position.BOTTOM_RIGHT_RIGHT && (segment == segments.get(Position.RIGHT) || segment == segments.get(Position.TOP_LEFT_TOP))) ||
						(current == Position.BOTTOM_RIGHT_BOTTOM && (segment == segments.get(Position.BOTTOM) || segment == segments.get(Position.TOP_LEFT_LEFT))) ||
						(current == Position.BOTTOM_LEFT_BOTTOM && (segment == segments.get(Position.BOTTOM) || segment == segments.get(Position.TOP_RIGHT_RIGHT)))) {
					updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
				}
			}	
		}

		if(current == Position.CENTER) {
			for(Position newPos: corners) {
				if(segment == segments.get(newPos) && segments.get(newPos) == Segment.FIELD && !traversed.contains(newPos)) {
					if(((newPos == Position.BOTTOM_LEFT_LEFT || newPos == Position.BOTTOM_LEFT_BOTTOM) && segments.get(Position.LEFT) != Segment.FIELD && segments.get(Position.BOTTOM) != Segment.FIELD) ||
							((newPos == Position.BOTTOM_RIGHT_RIGHT || newPos == Position.BOTTOM_RIGHT_BOTTOM) && segments.get(Position.RIGHT) == Segment.FIELD && segments.get(Position.BOTTOM) != Segment.FIELD) ||
							((newPos == Position.TOP_LEFT_LEFT || newPos == Position.TOP_LEFT_TOP) && segments.get(Position.LEFT) == Segment.FIELD && segments.get(Position.TOP) != Segment.FIELD) ||
							((newPos == Position.TOP_RIGHT_RIGHT || newPos == Position.TOP_RIGHT_TOP) && segments.get(Position.RIGHT) == Segment.FIELD && segments.get(Position.TOP) != Segment.FIELD)) {
						updatePositionData(id, newPos, name, segment, traversed, outerPositions, current);
					}
				}	
			}
		}
		
		// keep track of path if reaches the perimeter
		if(current != Position.CENTER) {
			outerPositions.add(current);
		}
	}
	
	/**
	 * Updates the position data and segment owners.
	 */
	private void updatePositionData(final int id, final Position newPos, final String name, final Segment segment,
			final Set<Position> traversed, final Set<Position> outerPositions, final Position current) {
		addPosition(newPos, name, id);
		traversed.add(current);
		updateSegmentOwners(segment, newPos, name, traversed, outerPositions, id);
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
	 * Returns owner of the identifier.
	 * @param id the identifier
	 * @return the owner of the connected segment
	 */
	public String getOwnerOfId(final int id) {
		for(Entry<Position, PositionData> entry : positions.entrySet()) {
			if(entry.getValue().getId() == id) {
				return getPositionOwner(entry.getKey());
			}
		}
		return null;
	}
	
	/**
	 * Removes the segment/position by id
	 * @param id the identifier
	 */
	@SuppressWarnings("rawtypes")
	public void removePositionsById(final int id) {
		Iterator<Entry<Position, PositionData>> posIterator = positions.entrySet().iterator();
		while (posIterator.hasNext()) {
			Map.Entry pairs = posIterator.next();
			if(((PositionData) pairs.getValue()).getId() == id) {
				posIterator.remove();
			}
		}
	}
	
	/**
	 * Sets whether there is a shield on this tile.
	 * @param position the position
	 */
	public void setShield(final Position position) {
		shield = position;
	}
	
	/**
	 * Returns whether this tile has a shield.
	 */
	public Position getShield() {
		return shield;
	}
	
	/**
	 * Returns whether this position has a divider.
	 * @param pos the position
	 * @return boolean
	 */
	public boolean isDivided(final Position pos) {
		return dividers.contains(pos);
	}
	
	/**
	 * Returns the divider property.
	 * @return a set of Position
	 */
	public Set<Position> getDividers() {
		return dividers;
	}
	
	/**
	 * Returns the segment based on id.
	 * @param id the connected segment identifier
	 * @return the Segment
	 */
	public Segment getSegmentById(final int id) {
		for(Entry<Position, PositionData> entry : positions.entrySet()) {
			PositionData data = entry.getValue();
			if(data.getId() == id) {
				return this.getSegment(entry.getKey());
			}
		}
		return Segment.UNDEFINED;
	}
	
	/**
	 * Rotates the tile based on the provided degrees.
	 * @param degrees the degrees
	 */
	public void rotate(final double degrees) {
		if(degrees == 90) {
			positionsRotated90();
			segmentsRotated90();
			shieldsRotated90();
		}
		else if(degrees == 180) {
			positionsRotated180();
			segmentsRotated180();
			shieldsRotated180();
		}
		else if(degrees == 270) {
			positionsRotated270();
			segmentsRotated270();
			shieldsRotated270();
		}
		setRotate(degrees);
	}
}
