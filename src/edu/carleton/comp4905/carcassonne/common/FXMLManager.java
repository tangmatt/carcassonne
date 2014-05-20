package edu.carleton.comp4905.carcassonne.common;

import javafx.fxml.FXMLLoader;

public class FXMLManager {
	public static final String FXML_DIR = "/edu/carleton/comp4905/carcassonne/fxml/";
	
	private FXMLManager() {
		// prevent instantiation of object
	}
	
	/**
	 * Retrieves a FXMLLoader object given the FXML file to load.
	 * @param clazz a Class
	 * @param name the FXML name (String)
	 * @return a FXMLLoader
	 */
	public static FXMLLoader getFXML(final Class<?> clazz, final String name) {
		return new FXMLLoader(clazz.getResource(FXML_DIR + name));
	}
}
