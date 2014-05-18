package edu.carleton.comp4905.carcassonne.client;

import java.util.EnumSet;
import java.util.Set;

import edu.carleton.comp4905.carcassonne.common.StringConstants;
import javafx.scene.image.ImageView;

public class GameTile extends ImageView implements Cloneable {
	private Segment top, right, bottom, left;
	private String name;
	
	public GameTile() {
		super();
	}
	
	public GameTile(GameTile tile) {
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
	public void setTile(String name, Segment top, Segment right, Segment bottom, Segment left) {
		this.name = name;
		setSegments(top, right, bottom, left);
		setImage(ResourceManager.getImage(StringConstants.TILES_DIR, name));
	}
	
	/**
	 * Sets the respective segments.
	 * @param top a Segment
	 * @param right a Segment
	 * @param bottom a Segment
	 * @param left a Segment
	 */
	private void setSegments(Segment top, Segment right, Segment bottom, Segment left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}
	
	/**
	 * Returns the name of the latest image file.
	 * @return
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
	
	public Segment getTopSegment() {
		return top;
	}
	
	public Segment getRightSegment() {
		return right;
	}
	
	public Segment getBottomSegment() {
		return bottom;
	}
	
	public Segment getLeftSegment() {
		return left;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
