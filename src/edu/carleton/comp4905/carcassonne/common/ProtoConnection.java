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
		service.getPool().execute(new Consumer());
	}

	@Override
	public void sendEvent(final Event event) {
		try {
			EventMessage.Event eventMessage = getEventMessage(event);
			eventMessage.writeDelimitedTo(peer.getOutputStream());
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
			boolean[] statuses = (boolean[])event.getProperty("statuses");
			for(int i=0; i<statuses.length; ++i)
				builder.addStatuses(statuses[i]);	
		}
		if(event.getProperty("names") != null) {
			String[] names = (String[])event.getProperty("names");
			for(int i=0; i<names.length; ++i)
				builder.addNames(names[i]);	
		}
		if(event.getProperty("finished") != null)
			builder.setFinished((boolean)event.getProperty("finished"));
		if(event.getProperty("gameInProgress") != null)
			builder.setGameInProgress((boolean)event.getProperty("gameInProgress"));
		if(event.getProperty("tile") != null)
			builder.setTile((String)event.getProperty("tile"));
		if(event.getProperty("row") != null)
			builder.setRow((int)event.getProperty("row"));
		if(event.getProperty("column") != null)
			builder.setColumn((int)event.getProperty("column"));
		if(event.getProperty("rotation") != null)
			builder.setRotation((int)event.getProperty("rotation"));
		
		return builder.build();
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
		
		return event;
	}
	
	private class Producer implements Runnable {
		@Override
		public void run() {
			while(running || !Thread.currentThread().isInterrupted()) {
				try {
					EventMessage.Event eventMessage = EventMessage.Event.parseDelimitedFrom(peer.getInputStream());
					if(!eventMessage.hasEventType() || !eventMessage.hasPlayerName())
						continue;
					Event event = getEvent(eventMessage);
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
