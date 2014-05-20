package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private final EventType eventType;
	private final String playerName;
	private final HashMap<String, Object> properties;
	
	public Event(final EventType eventType, final String playerName) {
		this.eventType = eventType;
		this.playerName = playerName;
		this.properties = new HashMap<String, Object>();
	}
	
	/**
	 * Returns the event type.
	 * @return
	 */
	public EventType getEventType() {
		return eventType;
	}
	
	/**
	 * Returns the player name.
	 * @return a String
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Adds an object mapped to a name.
	 * @param propertyName a String
	 * @param property an Object
	 */
	public void addProperty(final String propertyName, final Object property) {
		properties.put(propertyName, property);
	}
	
	/**
	 * Returns the object mapped to the specified name.
	 * @param propertyName a String
	 * @return an Object
	 */
	public Object getProperty(final String propertyName) {
		return properties.get(propertyName);
	}
}
