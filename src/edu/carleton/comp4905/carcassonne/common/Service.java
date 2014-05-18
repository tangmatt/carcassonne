package edu.carleton.comp4905.carcassonne.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service {
	protected Reactor reactor;
	protected PropertyLoader propertyLoader;
	protected ExecutorService pool;
	
	public Service() {
		propertyLoader = new PropertyLoader();
		reactor = new Reactor();
		pool = Executors.newCachedThreadPool();
	}
	
	public Reactor getReactor() {
		return reactor;
	}
	
	public PropertyLoader getPropertyLoader() {
		return propertyLoader;
	}
	
	public ExecutorService getPool() {
		return pool;
	}
	
	public void shutdown() {
		pool.shutdownNow();
	}
}
