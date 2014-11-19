package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class ProtoConnection extends Connection {
	private static final long serialVersionUID = 1L;

	public ProtoConnection(final Service service, final Socket peer) throws IOException {
		super(service, peer);
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
	public synchronized void broadcastEvent(final Event event, final ConcurrentMap<Address, Connection> connections) throws IOException {
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
			Player.Status[] statuses = new Player.Status[objArray.length];
			for(int i=0; i<statuses.length; ++i)
				statuses[i] = Player.Status.values()[(int)objArray[i]];
			event.addProperty("statuses", statuses);
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
		if(eventMessage.hasMode())
			event.addProperty("mode", Mode.values()[eventMessage.getMode()]);
		if(eventMessage.hasChecksum())
			event.addProperty("checksum", eventMessage.getChecksum());
		if(eventMessage.hasTilesLeft())
			event.addProperty("tilesLeft", eventMessage.getTilesLeft());
		if(eventMessage.hasRows())
			event.addProperty("rows", eventMessage.getRows());
		if(eventMessage.hasColumns())
			event.addProperty("columns", eventMessage.getColumns());
		if(eventMessage.hasMessageTitle())
			event.addProperty("messageTitle", eventMessage.getMessageTitle());
		if(eventMessage.hasFollowers())
			event.addProperty("followers", eventMessage.getFollowers());
		
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
			Player.Status[] statuses = (Player.Status[])event.getProperty("statuses");
			for(int i=0; i<statuses.length; ++i)
				builder.addStatuses(statuses[i].ordinal());	
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
		if(event.getProperty("mode") != null)
			builder.setMode(((Mode)event.getProperty("mode")).ordinal());
		if(event.getProperty("checksum") != null)
			builder.setChecksum((String)event.getProperty("checksum"));
		if(event.getProperty("tilesLeft") != null)
			builder.setTilesLeft((int)event.getProperty("tilesLeft"));
		if(event.getProperty("rows") != null)
			builder.setRows((int)event.getProperty("rows"));
		if(event.getProperty("columns") != null)
			builder.setColumns((int)event.getProperty("columns"));
		if(event.getProperty("messageTitle") != null)
			builder.setMessageTitle((String)event.getProperty("messageTitle"));
		if(event.getProperty("followers") != null)
			builder.setFollowers((int)event.getProperty("followers"));
		
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
					//running = false;
				}
			}
		}
	}
}