package edu.carleton.comp4905.carcassonne.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Acceptor;
import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.ProtoAcceptor;
import edu.carleton.comp4905.carcassonne.common.Service;

public class Server extends Service implements Runnable {
	private final ConcurrentMap<Address, Connection> connections;
	private ServerSocket listener;
	private Acceptor acceptor;
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
			acceptor = new ProtoAcceptor(this, listener);
			running = false;
			initialize();
		} catch (IOException e) {
			// TODO controller is null, need to fix
			/*new MessageDialog(controller.getAnchorPane().getScene().getWindow(),
					controller.getServerClient(),
					StringConstants.ERR_TITLE,
					e.getMessage())
			.show();*/
		}
	}
	
	/**
	 * Initializes the reactor with event handlers and instantiates them.
	 */
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
	
	/**
	 * Returns the map of Connections.
	 * @return a Map
	 */
	public ConcurrentMap<Address, Connection> getConnections() {
		return connections;
	}
	
	/**
	 * Returns the server socket.
	 * @return a ServerSocket
	 */
	public ServerSocket getServerSocket() {
		return listener;
	}
	
	/**
	 * Returns the ServerController object
	 * @return a ServerController
	 */
	public ServerController getController() {
		return controller;
	}
	
	/**
	 * Sets the ServerController object.
	 * @param controller a ServerController
	 */
	public void setController(final ServerController controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		controller.addMessageEntry(MessageType.INFO, "Server listening on port " + PORT);
		running = true;
		while(running) {
			try {
				Connection connection = acceptor.accept();
				pool.execute(connection);
			} catch (IOException e) {
				running = false;
			}
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		running = false;
		
		for(Connection connection : connections.values())
			connection.close();
		
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		controller.addMessageEntry(MessageType.INFO, "Server is now offline");
	}
}
