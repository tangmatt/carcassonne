package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Connection implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	protected final Service service;
	protected final Socket peer;
	protected static boolean running;
	protected final LinkedBlockingQueue<Event> buffer;
	
	public Connection(final Service service, final Socket peer) {
		this.service = service;
		this.peer = peer;
		running = false;
		this.buffer = new LinkedBlockingQueue<Event>();
	}
	
	protected class Consumer implements Runnable {
		private final Connection connection;

		public Consumer(final Connection connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			while(running) {
				try {
					Event event = buffer.take();
					event.addProperty("connection", connection);
					event.addProperty("address", peer.getInetAddress().getHostAddress());
					event.addProperty("port", peer.getPort());
					System.out.println(event);
					if(event != null)
						service.getReactor().dispatch(event);
				} catch (Exception e) {
					running = false;
				}
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
	public void close() {
		try {
			running = false;
			peer.close();
			service.getPool().shutdownNow();
		} catch (Exception e) {
			// do nothing
		}
	}
	
	/**
	 * Sends an event.
	 * @param event an Event
	 * @throws IOException 
	 */
	public abstract void sendEvent(Event event) throws IOException;
	
	/**
	 * Broadcasts an event to all connected services.
	 * @param event an Event
	 * @throws IOException 
	 */
	public abstract void broadcastEvent(Event event, ConcurrentMap<Address, Connection> connections) throws IOException;
}