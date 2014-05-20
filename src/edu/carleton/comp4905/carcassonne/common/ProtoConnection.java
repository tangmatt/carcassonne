package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProtoConnection extends Connection {
	private static final long serialVersionUID = 1L;
	protected final Socket peer;
	private boolean running;

	public ProtoConnection(final Service service, final Socket peer) throws IOException {
		super(service);
		this.peer = peer;
		this.running = false;
	}
	
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
	public Event getEvent() throws InterruptedException {
		return buffer.take();
	}

	@Override
	public void sendEvent(final Event event) {
		try {
			buffer.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class Producer implements Runnable {
		@Override
		public void run() {
			while(running || !Thread.currentThread().isInterrupted()) {
				try {
					Event event = getEvent();
					/*EventMessage.Event eventMessage = EventMessage.Event.newBuilder()
														.setEventType(event.getEventType().ordinal())
														.setPlayerName(event.getPlayerName())
														.build();
					eventMessage.writeTo(peer.getOutputStream());*/
					new ObjectOutputStream(peer.getOutputStream()).writeObject(event);
				} catch (IOException e) {
					Thread.currentThread().interrupt();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
	
	private class Consumer implements Runnable {
		private final ProtoConnection connection;
		
		public Consumer(final ProtoConnection connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			try {
				while(running || !Thread.currentThread().isInterrupted()) {
					/*if(service.getClass() == Server.class) {
						((Server)service).getController().addMessageEntry(MessageType.INFO, "Received Event Message");
					}
					EventMessage.Event eventMessage = EventMessage.Event.parseFrom(peer.getInputStream());
					if(!eventMessage.hasEventType())
						continue;
					Event event = new Event(EventType.values()[eventMessage.getEventType()], eventMessage.getPlayerName());*/
					Event event = (Event) new ObjectInputStream(peer.getInputStream()).readObject();
					event.addProperty("connection", connection);
					event.addProperty("address", peer.getInetAddress().getHostAddress());
					event.addProperty("port", peer.getPort());
					if(event != null)
						service.getReactor().dispatch(event);
				}
			} catch (IOException e) {
				Thread.currentThread().interrupt();
			} catch (ClassNotFoundException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
