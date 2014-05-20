package edu.carleton.comp4905.carcassonne.common;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyLoader {
	private final Properties properties;
	private final String configDir = "config/";
	
	public PropertyLoader() {
		this.properties = new Properties();
	}
	
	/**
	 * Loads the properties file.
	 * @param name
	 */
	public void loadProperty(final String name) {
		try {
			properties.load(new FileInputStream(configDir + name));
		} catch (Exception e) {
			System.err.println("Unable to locate property file '" + name + "' on your system.");
		}
	}
	
	/**
	 * Returns the property associated with the given name.
	 * @param name a name (String)
	 * @return a String
	 */
	public String getProperty(final String name) {
		return properties.getProperty(name);
	}
	
	/**
	 * Returns the Properties object.
	 * @return a Properties
	 */
	public Properties getProperties() {
		return properties;
	}
}
