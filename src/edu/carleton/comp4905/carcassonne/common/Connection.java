package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Connection implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	protected final Service service;
	protected final Socket peer;
	protected final boolean running;
	protected final LinkedBlockingQueue<Event> buffer;
	
	public Connection(final Service service, final Socket peer) {
		this.service = service;
		this.peer = peer;
		this.running = false;
		this.buffer = new LinkedBlockingQueue<Event>();
	}
	
	/**
	 * Returns the Service object.
	 * @return a Service
	 */
	public Service getService() {
		return service;
	}
	
	/**
	 * Returns the Socket object.
	 * @return a Socket
	 */
	public Socket getPeer() {
		return peer;
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
	 * Sends an event.
	 * @param event an Event
	 */
	public abstract void sendEvent(Event event);
	
	/**
	 * Broadcasts an event to all connected services.
	 * @param event an Event
	 */
	public abstract void broadcastEvent(Event event, ConcurrentMap<Address, Connection> connections);
}
