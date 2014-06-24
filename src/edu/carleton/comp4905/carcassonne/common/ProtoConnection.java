package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class ProtoConnection extends Connection {
	private static final long serialVersionUID = 1L;
	private boolean running;

	public ProtoConnection(final Service service, final Socket peer) throws IOException {
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
			EventMessage.Event eventMessage = getEventMessage(event);
			eventMessage.writeDelimitedTo(peer.getOutputStream());
		} catch (IOException e) {
			// do nothing
		}
	}
	
	@Override
	public synchronized void broadcastEvent(final Event event, final ConcurrentMap<Address, Connection> connections) {
		for(Address address : connections.keySet()) {
			Connection connection = connections.get(address);
			connection.sendEvent(event);
		}
	}
	
	/**
	 * Returns an Event.
	 * @param eventMessage an EventMessage
	 * @return an Event
	 */
	protected Event getEvent(final EventMessage.Event eventMessage) {
		Event event = new Event(EventType.values()[eventMessage.getEventType()], eventMessage.getPlayerName());
		// add current service's information to event
		event.addProperty("connection", this);
		event.addProperty("address", peer.getInetAddress().getHostAddress());
		event.addProperty("port", peer.getPort());
		
		if(eventMessage.hasNumOfPlayers())
			event.addProperty("numOfPlayers", eventMessage.getNumOfPlayers());
		if(eventMessage.hasSuccess())
			event.addProperty("success", eventMessage.getSuccess());
		if(eventMessage.hasMessage())
			event.addProperty("message", eventMessage.getMessage());
		if(eventMessage.getStatusesList() != null){
			Object[] objArray = eventMessage.getStatusesList().toArray();
			event.addProperty("statuses", Arrays.copyOf(objArray, objArray.length, Boolean[].class));
		}
		if(eventMessage.getNamesList() != null) {
			Object[] objArray = eventMessage.getNamesList().toArray();
			event.addProperty("names", Arrays.copyOf(objArray, objArray.length, String[].class));
		}
		if(eventMessage.hasFinished())
			event.addProperty("finished", eventMessage.getFinished());
		if(eventMessage.hasGameInProgress())
			event.addProperty("gameInProgress", eventMessage.getGameInProgress());
		if(eventMessage.hasTile())
			event.addProperty("tile", eventMessage.getTile());
		if(eventMessage.hasRow())
			event.addProperty("row", eventMessage.getRow());
		if(eventMessage.hasColumn())
			event.addProperty("column", eventMessage.getColumn());
		if(eventMessage.hasRotation())
			event.addProperty("rotation", eventMessage.getRotation());
		if(eventMessage.hasMeeple())
			event.addProperty("meeple", eventMessage.getMeeple());
		if(eventMessage.hasPosition())
			event.addProperty("position", Position.values()[eventMessage.getPosition()]);
		if(eventMessage.hasShield())
			event.addProperty("shield", eventMessage.getShield());
		if(eventMessage.hasTarget())
			event.addProperty("target", eventMessage.getTarget());
		if(eventMessage.hasPoints())
			event.addProperty("points", eventMessage.getPoints());
		if(eventMessage.hasTargetIndex())
			event.addProperty("targetIndex", eventMessage.getTargetIndex());
		
		return event;
	}
	
	/**
	 * Returns an Event Message.
	 * @param event an Event
	 * @return EventMessage.Event
	 */
	protected EventMessage.Event getEventMessage(final Event event) {
		EventMessage.Event.Builder builder = EventMessage.Event.newBuilder()
				.setEventType(event.getEventType().ordinal())
				.setPlayerName(event.getPlayerName());
		
		if(event.getProperty("numOfPlayers") != null)
			builder.setNumOfPlayers((int)event.getProperty("numOfPlayers"));
		if(event.getProperty("success") != null)
			builder.setSuccess((boolean)event.getProperty("success"));
		if(event.getProperty("message") != null)
			builder.setMessage((String)event.getProperty("message"));
		if(event.getProperty("statuses") != null) {
			Boolean[] statuses = (Boolean[])event.getProperty("statuses");
			for(int i=0; i<statuses.length; ++i)
				builder.addStatuses(statuses[i]);	
		}
		if(event.getProperty("names") != null) {
			String[] names = (String[])event.getProperty("names");
			for(int i=0; i<names.length; ++i)
				builder.addNames(names[i]);	
		}
		if(event.getProperty("finished") != null)
			builder.setFinished((Boolean)event.getProperty("finished"));
		if(event.getProperty("gameInProgress") != null)
			builder.setGameInProgress((Boolean)event.getProperty("gameInProgress"));
		if(event.getProperty("tile") != null)
			builder.setTile((String)event.getProperty("tile"));
		if(event.getProperty("row") != null)
			builder.setRow((int)event.getProperty("row"));
		if(event.getProperty("column") != null)
			builder.setColumn((int)event.getProperty("column"));
		if(event.getProperty("rotation") != null)
			builder.setRotation((int)event.getProperty("rotation"));
		if(event.getProperty("meeple") != null)
			builder.setMeeple((int)event.getProperty("meeple"));
		if(event.getProperty("position") != null)
			builder.setPosition(((Position)event.getProperty("position")).ordinal());
		if(event.getProperty("shield") != null)
			builder.setShield((boolean)event.getProperty("shield"));
		if(event.getProperty("target") != null)
			builder.setTarget((String)event.getProperty("target"));
		if(event.getProperty("points") != null)
			builder.setPoints((int)event.getProperty("points"));
		if(event.getProperty("targetIndex") != null)
			builder.setTargetIndex((int)event.getProperty("targetIndex"));
		
		return builder.build();
	}
	
	private class Producer implements Runnable {
		@Override
		public void run() {
			while(running) {
				try {
					EventMessage.Event eventMessage = EventMessage.Event.parseDelimitedFrom(peer.getInputStream());
					if(!eventMessage.hasEventType() || !eventMessage.hasPlayerName())
						continue;
					Event event = getEvent(eventMessage);
					buffer.put(event);
				} catch (Exception e) {
					running = false;
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