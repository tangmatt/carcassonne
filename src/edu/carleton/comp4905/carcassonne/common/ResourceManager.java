package edu.carleton.comp4905.carcassonne.common;

import javafx.scene.image.Image;

public class ResourceManager {
	public static final String RESOURCES_DIR = "/edu/carleton/comp4905/carcassonne/resources/";
	public static final String TILES_DIR = "/edu/carleton/comp4905/carcassonne/resources/tiles/";
	
	private ResourceManager() {
		// prevent instantiation of object
	}
	
	/**
	 * Retrieves an image from the resources folder and returns the Image object containing the image.
	 * @param name the image name (String)
	 * @return an Image
	 */
	public static Image getImageFromResources(final String name) {
		return new Image(RESOURCES_DIR + name);
	}
	
	/**
	 * Retrieves an image from the tiles folder and returns the Image object containing the image.
	 * @param name the image name (String)
	 * @return an Image
	 */
	public static Image getImageFromTiles(final String name) {
		return new Image(TILES_DIR + name);
	}
}
