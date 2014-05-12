package edu.carleton.comp4905.carcassonne.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Acceptor;
import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.EventHandler;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.ProtoAcceptor;
import edu.carleton.comp4905.carcassonne.common.Service;

public class Server extends Service implements Runnable {
	private ConcurrentMap<Address, Connection> connections;
	private ServerSocket listener;
	private Acceptor acceptor;
	private Logger logger;
	private ServerController controller;
	private boolean running;
	
	public static int PORT = 5000;
	
	public Server() {
		this(PORT);
	}
	
	public Server(final int port) {
		super();
		PORT = port;
		connections = new ConcurrentHashMap<Address, Connection>();
		try {
			listener = new ServerSocket(PORT);
			listener.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		acceptor = new ProtoAcceptor(this, listener);
		logger = new Logger();
		running = false;
		initialize();
	}
	
	private void initialize() {
		propertyLoader.loadProperty("server.properties");
		for(Object eventType : propertyLoader.getProperties().keySet()) {
			String handlerName = propertyLoader.getProperty((String)eventType);
			if(handlerName != null) {
				try {
					reactor.addHandler(EventType.valueOf((String)eventType), getEventHandler(propertyLoader.getProperties(), handlerName));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public ConcurrentMap<Address, Connection> getConnections() {
		return connections;
	}
	
	public ServerSocket getServerSocket() {
		return listener;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public ServerController getController() {
		return controller;
	}
	
	public void setController(ServerController controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		controller.addMessageEntry(MessageType.INFO, "Server listening on port " + PORT);
		running = true;
		while(running) {
			try {
				//controller.addMessageEntry(MessageType.INFO, "Waiting for active connections...");
				Connection connection = acceptor.accept();
				executors.execute(connection);
			} catch (IOException e) {
				running = false;
			}
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		running = false;
		
		//for(Connection connection : connections.values())
		//	connection.close();
		
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		controller.addMessageEntry(MessageType.INFO, "Server is now offline");
	}
	
	/**
	 * Creates an instance of an EventHandler
	 * @param properties the properties
	 * @param handlerName the handler name
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public EventHandler getEventHandler(Properties properties, final String handlerName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// Instantiate the handler
		@SuppressWarnings("unchecked")
		Class<EventHandler> clazz = (Class<EventHandler>)Class.forName(handlerName);
		return clazz.newInstance();
	}
}
