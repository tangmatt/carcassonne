package edu.carleton.comp4905.carcassonne.common;

import java.util.EnumMap;
import java.util.Map;

public class Reactor {
	private final Map<EventType, EventHandler> handlers;
	
	public Reactor() {
		this.handlers = new EnumMap<EventType, EventHandler>(EventType.class);
	}
	
	/**
	 * Adds the event handler and maps it to an event type.
	 * @param eventType an EventType
	 * @param handler an EventHandler
	 */
	public void addHandler(final EventType eventType, final EventHandler handler) {
		if(eventType == null || handler == null)
			return;
		handlers.put(eventType, handler);
	}
	
	/**
	 * Removes an event handler given an event type.
	 * @param eventType an EventType
	 * @return an EventHandler
	 */
	public EventHandler removeHandler(final EventType eventType) {
		return handlers.remove(eventType);
	}
	
	/**
	 * Dispatches the event handler associated with the specified event.
	 * @param event an Event
	 */
	public void dispatch(final Event event) {
		if(event != null) {
			EventHandler handler = handlers.get(event.getEventType()) != null ? handlers.get(event.getEventType()) : handlers.get(EventType.UNKNOWN);
			if(handler != null)
				handler.handleEvent(event);
		}
	}
}
