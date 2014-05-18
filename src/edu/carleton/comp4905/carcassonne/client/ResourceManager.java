package edu.carleton.comp4905.carcassonne.client;

import javafx.scene.image.Image;

public class ResourceManager {
	private ResourceManager() {
		// prevent instantiation of object
	}
	
	public static Image getImage(String path, String name) {
		return new Image(path + name);
	}
}
