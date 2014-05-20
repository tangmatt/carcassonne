package edu.carleton.comp4905.carcassonne.common;

import java.util.Properties;
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
	
	/**
	 * Returns the Reactor object.
	 * @return a Reactor
	 */
	public Reactor getReactor() {
		return reactor;
	}
	
	/**
	 * Returns the PropertyLoader object.
	 * @return a PropertyLoader
	 */
	public PropertyLoader getPropertyLoader() {
		return propertyLoader;
	}
	
	/**
	 * Returns the ExecutorService object.
	 * @return an ExecutorService
	 */
	public ExecutorService getPool() {
		return pool;
	}
	
	/**
	 * Shuts down the service.
	 */
	public void shutdown() {
		pool.shutdownNow();
	}
	
	/**
	 * Creates an instance of an EventHandler.
	 * @param properties the properties
	 * @param handlerName the handler name
	 * @return an EventHandler
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected EventHandler getEventHandler(final Properties properties, final String handlerName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// Instantiate the handler
		@SuppressWarnings("unchecked")
		Class<EventHandler> clazz = (Class<EventHandler>)Class.forName(handlerName);
		return clazz.newInstance();
	}
}
