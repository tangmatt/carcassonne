package edu.carleton.comp4905.carcassonne.common;

import java.util.EnumMap;
import java.util.Map;

public class Reactor {
	private Map<EventType, EventHandler> handlers;
	
	public Reactor() {
		this.handlers = new EnumMap<EventType, EventHandler>(EventType.class);
	}
	
	public void addHandler(final EventType eventType, final EventHandler handler) {
		if(eventType == null || handler == null)
			return;
		handlers.put(eventType, handler);
	}
	
	public EventHandler removeHandler(final EventType eventType) {
		return handlers.remove(eventType);
	}
	
	public void dispatch(final Event event) {
		if(event != null) {
			EventHandler handler = handlers.get(event.getEventType()) != null ? handlers.get(event.getEventType()) : handlers.get(EventType.UNKNOWN);
			if(handler != null)
				handler.handleEvent(event);
		}
	}
}
