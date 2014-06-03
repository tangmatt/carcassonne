package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Position;
import javafx.scene.image.ImageView;

public class Follower extends ImageView {
	protected Position position;
	
	public Follower(final Position position) {
		this.position = position;
	}
}
