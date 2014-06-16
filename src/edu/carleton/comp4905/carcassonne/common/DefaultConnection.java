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
		service.getPool().execute(new Producer());
		service.getPool().execute(new Consumer(this));
	}

	@Override
	public synchronized void sendEvent(final Event event) {
		try {
			System.out.println(event.getEventType() + " ------ " + service.getClass().getName());
			new ObjectOutputStream(peer.getOutputStream()).writeObject(event);
		} catch (IOException e) {
			// do nothing
		}
	}

	@Override
	public synchronized void broadcastEvent(final Event event, final ConcurrentMap<Address, Connection> connections) {
		System.out.println(event.getEventType() + " ------ " + service.getClass().getName());
		for(Address address : connections.keySet()) {
			Connection connection = connections.get(address);
			connection.sendEvent(event);
		}
	}

	private class Producer implements Runnable {
		@Override
		public void run() {
			while(running) {
				try {
					Event event = (Event) new ObjectInputStream(peer.getInputStream()).readObject();
					buffer.put(event);
				} catch (Exception e) {
					running = false;
				}
			}
		}
	}

	private class Consumer implements Runnable {
		private final DefaultConnection connection;

		public Consumer(final DefaultConnection connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			try {
				while(running) {
					Event event = buffer.take();
					event.addProperty("connection", connection);
					event.addProperty("address", peer.getInetAddress().getHostAddress());
					event.addProperty("port", peer.getPort());
					if(event != null)
						service.getReactor().dispatch(event);
				}
			} catch (Exception e) {
				running = false;
			}
		}
	}
}