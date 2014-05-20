package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Connection implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	protected final Service service;
	protected final boolean running;
	protected final LinkedBlockingQueue<Event> buffer;
	
	public Connection(final Service service) {
		this.service = service;
		this.running = false;
		this.buffer = new LinkedBlockingQueue<Event>();
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				Event event = getEvent();
				service.getPool().execute(new Runnable() {
					@Override
					public void run() {
						service.getReactor().dispatch(event);
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the Service object.
	 * @return a Service
	 */
	public Service getService() {
		return service;
	}
	
	/**
	 * Returns the buffer.
	 * @return a LinkedBlockingQueue<Event>
	 */
	public LinkedBlockingQueue<Event> getBuffer() {
		return buffer;
	}
	
	/**
	 * Closes the socket and other relations.
	 */
	public abstract void close();
	
	/**
	 * Returns an Event object.
	 * @return an Event
	 * @throws InterruptedException
	 */
	public abstract Event getEvent() throws InterruptedException;
	
	/**
	 * Sends an event.
	 * @param event an Event
	 */
	public abstract void sendEvent(Event event);
}
