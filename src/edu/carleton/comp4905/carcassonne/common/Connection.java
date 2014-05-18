package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Connection implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	protected Service service;
	protected boolean running;
	protected LinkedBlockingQueue<Event> buffer;
	
	public Connection(Service service) {
		this.service = service;
		this.running = false;
		this.buffer = new LinkedBlockingQueue<Event>();
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				Event event = getEvent();
				service.getPool().execute(new Runnable() {
					@Override
					public void run() {
						service.getReactor().dispatch(event);
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Service getService() {
		return service;
	}
	
	public LinkedBlockingQueue<Event> getBuffer() {
		return buffer;
	}
	
	public abstract void close();
	public abstract Event getEvent() throws InterruptedException;
	public abstract void sendEvent(Event event);
}
