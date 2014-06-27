package edu.carleton.comp4905.carcassonne.client;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Connector;
import edu.carleton.comp4905.carcassonne.common.DefaultConnector;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ProtoConnector;
import edu.carleton.comp4905.carcassonne.common.Serializer;
import edu.carleton.comp4905.carcassonne.common.Service;
import edu.carleton.comp4905.carcassonne.common.TileManager;

public class Game extends Service implements Runnable {
	private final String playerName;
	private final Address address;
	private Connector connector;
	private Connection connection;
	private final GameController controller;
	
	public Game(final String playerName, final Address address, final GameController gameController) {
		super();
		this.address = address;
		this.playerName = playerName;
		if(serializer == Serializer.JAVA_SERIALIZE)
			this.connector = new DefaultConnector(this);
		else if(serializer == Serializer.GOOGLE_PROTOBUF)
			this.connector = new ProtoConnector(this);
		this.controller = gameController;
	}

	@Override
	public void run() {
		try {
			Event event = new Event(EventType.JOIN_REQUEST, playerName);
			event.addProperty("checksum", TileManager.getInstance().checksum());
			connection = connector.connect(address.getHostname(), address.getPort());
			pool.execute(connection);
			connection.sendEvent(event);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			PlatformManager.run(new Runnable() {
				@Override
				public void run() {
					controller.blurGame(true);
					new MessageDialog(controller.getGridPane().getScene().getWindow(),
							controller.getGameClient(),
							LocalMessages.getString("ErrTitle"),
							e.getMessage(),
							true)
					.show();
				}
			});
		}
	}
	
	/**
	 * Returns the client's player name.
	 * @return a String
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Returns the client's address information.
	 * @return an Address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * Returns the established Connection object.
	 * @return a Connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Returns the GameController object.
	 * @return a GameController
	 */
	public GameController getGameController() {
		return controller;
	}
	
	@Override
	public void shutdown() {
		if(connection != null)
			connection.close();
		super.shutdown();
	}
}
