package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Position;
import javafx.scene.image.ImageView;

public class Follower extends ImageView {
	protected Position position;
	protected String owner;
	
	public Follower(final Position position) {
		this(position, null);
	}
	
	public Follower(final Position position, final String owner) {
		this.position = position;
		this.owner = owner;
	}
	
	/**
	 * Returns the position.
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Returns the owner name.
	 * @return the owner name
	 */
	public String getOwner() {
		return owner;
	}
}
