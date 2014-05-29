package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;

public class DefaultConnection extends Connection {
	private static final long serialVersionUID = 1L;
	private boolean running;

	public DefaultConnection(final Service service, final Socket peer) throws IOException {
		super(service, peer);
		this.running = false;
	}
	
	@Override
	public Socket getPeer() {
		return peer;
	}
	
	@Override
	public void close() {
		try {
			running = false;
			service.getPool().shutdown();
			peer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		running = true;
		service.getPool().execute(new Producer(this));
		service.getPool().execute(new Consumer());
	}
	
	@Override
	public void sendEvent(final Event event) {
		try {
			new ObjectOutputStream(peer.getOutputStream()).writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void broadcastEvent(final Event event, final ConcurrentMap<Address, Connection> connections) {
		for(Address address : connections.keySet()) {
			Connection connection = connections.get(address);
			connection.sendEvent(event);
		}
	}
	
	private class Producer implements Runnable {
		private final DefaultConnection connection;
		
		public Producer(final DefaultConnection connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			while(running || !Thread.currentThread().isInterrupted()) {
				try {
					Event event = (Event) new ObjectInputStream(peer.getInputStream()).readObject();
					event.addProperty("connection", connection);
					event.addProperty("address", peer.getInetAddress().getHostAddress());
					event.addProperty("port", peer.getPort());
					buffer.put(event);
				} catch (Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	private class Consumer implements Runnable {
		@Override
		public void run() {
			try {
				while(running || !Thread.currentThread().isInterrupted()) {
					Event event = buffer.take();
					if(event != null)
						service.getReactor().dispatch(event);
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
