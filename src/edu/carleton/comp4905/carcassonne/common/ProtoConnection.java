package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoConnection extends Connection {
	private static final long serialVersionUID = 1L;
	protected Socket peer;
	protected BlockingQueue<Event> buffer;

	public ProtoConnection(Service service, Socket peer) throws IOException {
		super(service);
		this.peer = peer;
		this.buffer = new LinkedBlockingQueue<Event>();
	}
	
	public Socket getPeer() {
		return peer;
	}
	
	@Override
	public void close() {
		try {
			peer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		new Thread(new Producer()).start();
		new Thread(new Consumer(this)).start();
	}

	@Override
	public Event getEvent() throws InterruptedException {
		return buffer.take();
	}

	@Override
	public void sendEvent(Event event) {
		try {
			buffer.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class Producer implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
					Event event = getEvent();
					/*EventMessage.Event eventMessage = EventMessage.Event.newBuilder()
														.setEventType(event.getEventType().ordinal())
														.setPlayerName(event.getPlayerName())
														.build();
					eventMessage.writeTo(peer.getOutputStream());*/
					new ObjectOutputStream(peer.getOutputStream()).writeObject(event);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class Consumer implements Runnable {
		private ProtoConnection connection;
		
		public Consumer(ProtoConnection connection) {
			this.connection = connection;
		}
		
		@Override
		public void run() {
			try {
				while(true) {
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
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
