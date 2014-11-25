package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;

public class DefaultConnection extends Connection {
	private static final long serialVersionUID = 1L;

	public DefaultConnection(final Service service, final Socket peer) throws IOException {
		super(service, peer);
	}

	@Override
	public void run() {
		running = true;
		service.getPool().execute(new Producer());
		service.getPool().execute(new Consumer(this));
	}

	@Override
	public synchronized void sendEvent(final Event event) throws IOException {
		new ObjectOutputStream(peer.getOutputStream()).writeObject(event);
	}

	@Override
	public synchronized void broadcastEvent(final Event event, final ConcurrentMap<Address, Connection> connections) throws IOException {
		System.out.println("Broadcasting " + event.getEventType() + " to " + connections.size() + " players:");
		for(Address address : connections.keySet()) {
			Connection connection = connections.get(address);
			connection.sendEvent(event);
			System.out.println("\t" + event.getEventType() + " to " + connection.getPeer().getPort());
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
					//running = false;
				}
			}
		}
	}
}