package edu.carleton.comp4905.carcassonne.client;

import java.util.EnumSet;
import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView implements Cloneable {
	private Segment top, right, bottom, left;
	private String name;
	
	public GameTile() {
		super();
	}
	
	public GameTile(final GameTile tile) {
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
	private void setSegments(final Segment top, final Segment right, final Segment bottom, final Segment left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
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
