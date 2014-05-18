package edu.carleton.comp4905.carcassonne.client;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Connector;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.ProtoConnector;
import edu.carleton.comp4905.carcassonne.common.Service;

public class Game extends Service implements Runnable {
	private String playerName;
	private Address address;
	private Connector connector;
	private Connection connection;
	
	public Game(final String playerName, final Address address) {
		this.address = address;
		this.playerName = playerName;
		this.connector = new ProtoConnector(this);
	}
	
	@Override
	public void run() {
		try {
			connection = connector.connect(address.getHostname(), address.getPort());
			pool.execute(connection);
			connection.sendEvent(new Event(EventType.JOIN_REQUEST, playerName));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized String getPlayerName() {
		return playerName;
	}
	
	public synchronized Address getAddress() {
		return address;
	}
	
	public synchronized Connection getConnection() {
		return connection;
	}
	
	@Override
	public synchronized void shutdown() {
		connection.close();
		super.shutdown();
	}
}
