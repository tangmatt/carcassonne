package edu.carleton.comp4905.carcassonne.common;

import java.io.Serializable;

public abstract class Connection implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	protected Service service;
	protected boolean running;
	
	public Connection(Service service) {
		this.service = service;
		this.running = false;
	}
	
	@Override
	public void run() {
		while(running) {
			try {
				Event event = getEvent();
				service.getExecutor().execute(new Runnable() {
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
	
	public abstract void close();
	public abstract Event getEvent() throws InterruptedException;
	public abstract void sendEvent(Event event);
}
