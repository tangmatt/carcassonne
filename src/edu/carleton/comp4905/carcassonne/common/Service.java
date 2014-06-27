package edu.carleton.comp4905.carcassonne.common;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.carleton.comp4905.carcassonne.client.Game;
import edu.carleton.comp4905.carcassonne.server.Server;

public abstract class Service {
	protected final Reactor reactor;
	protected Serializer serializer;
	protected Mode mode;
	protected String properties;
	protected final ExecutorService pool;
	public static final String FILENAME = "config/app.properties";
	
	public Service() {
		reactor = new Reactor();
		pool = Executors.newCachedThreadPool();
		initGame();
		initReactor();
	}
	
	/**
	 * Initializes the game settings.
	 */
	protected void initGame() {
		PropertyLoader propertyLoader = new PropertyLoader();
		propertyLoader.loadProperty(FILENAME);
		for(Object propertyType : propertyLoader.getProperties().keySet()) {
			String value = propertyLoader.getProperty((String)propertyType);
			if(value != null) {
				try {
					if(((String)propertyType).equalsIgnoreCase("SERIALIZER"))
						serializer = Serializer.valueOf(value);
					else if(((String)propertyType).equalsIgnoreCase("MODE"))
						mode = Mode.valueOf(value);
					else if(((String)propertyType).equalsIgnoreCase("CLIENT") && this.getClass() == Game.class)
						properties = value;
					else if(((String)propertyType).equalsIgnoreCase("SERVER") && this.getClass() == Server.class)
						properties = value;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Initializes the reactor with event handlers and instantiates them.
	 */
	protected void initReactor() {
		PropertyLoader propertyLoader = new PropertyLoader();
		propertyLoader.loadProperty(properties);
		for(Object propertyType : propertyLoader.getProperties().keySet()) {
			String handlerName = propertyLoader.getProperty((String)propertyType);
			if(handlerName != null) {
				try {
					reactor.addHandler(EventType.valueOf((String)propertyType), getEventHandler(propertyLoader.getProperties(), handlerName));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Returns the Reactor object.
	 * @return a Reactor
	 */
	public Reactor getReactor() {
		return reactor;
	}
	
	/**
	 * Returns the ExecutorService object.
	 * @return an ExecutorService
	 */
	public ExecutorService getPool() {
		return pool;
	}
	
	/**
	 * Returns the gameplay mode.
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}
	
	/**
	 * Sets the gameplay mode.
	 * @param mode the mode
	 */
	public void setMode(final Mode mode) {
		this.mode = mode;
	}
	
	/**
	 * Returns the serializer method.
	 * @return the serializer
	 */
	public Serializer getSerializer() {
		return serializer;
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
