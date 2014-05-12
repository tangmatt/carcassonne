package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private EventType eventType;
	private String playerName;
	private HashMap<String, Object> properties;
	
	public Event(EventType eventType, String playerName) {
		this.eventType = eventType;
		this.playerName = playerName;
		this.properties = new HashMap<String, Object>();
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void addProperty(final String propertyName, final Object property) {
		properties.put(propertyName, property);
	}
	
	public Object getProperty(final String propertyName) {
		return properties.get(propertyName);
	}
}
