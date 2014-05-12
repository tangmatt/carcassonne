package edu.carleton.comp4905.carcassonne.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyLoader {
	private Properties properties;
	private final String configDir = "config" + File.separator;
	
	public PropertyLoader() {
		this.properties = new Properties();
	}
	
	public void loadProperty(final String name) {
		try {
			properties.load(new FileInputStream(configDir + name));
		} catch (Exception e) {
			System.err.println("Unable to locate property file '" + name + "' on your system.");
		}
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	public Properties getProperties() {
		return properties;
	}
}
