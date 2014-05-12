package edu.carleton.comp4905.carcassonne.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service {
	protected Reactor reactor;
	protected PropertyLoader propertyLoader;
	protected ExecutorService executors;
	
	public Service() {
		propertyLoader = new PropertyLoader();
		reactor = new Reactor();
		executors = Executors.newCachedThreadPool();
	}
	
	public Reactor getReactor() {
		return reactor;
	}
	
	public PropertyLoader getPropertyLoader() {
		return propertyLoader;
	}
	
	public ExecutorService getExecutor() {
		return executors;
	}
	
	public void shutdown() {
		executors.shutdown();
	}
}
