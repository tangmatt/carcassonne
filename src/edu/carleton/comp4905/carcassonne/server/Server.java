package edu.carleton.comp4905.carcassonne.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Acceptor;
import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.DefaultAcceptor;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ProtoAcceptor;
import edu.carleton.comp4905.carcassonne.common.Serializer;
import edu.carleton.comp4905.carcassonne.common.Service;

public class Server extends Service implements Runnable {
	private final ConcurrentMap<Address, Connection> connections;
	private ServerSocket listener;
	private Acceptor acceptor;
	private ServerController controller;
	private boolean running;
	private boolean gameInProgress;
	private final LinkedList<String> queue;
	private final List<String> players;
	private String currPlayer;
	
	public final static int INITIAL_ROWS = 8;
	public final static int INITIAL_COLS = 9;
	public static int PORT = 5000;
	
	public Server() {
		this(PORT, Mode.SYNC);
	}
	
	public Server(final int port, final Mode mode) {
		super();
		PORT = port;
		this.connections = new ConcurrentHashMap<Address, Connection>();
		this.running = false;
		this.gameInProgress = false;
		this.queue = new LinkedList<String>();
		this.players = new ArrayList<String>();
		this.mode = mode;
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
	 * Returns the current turn index.
	 * @return the turn index
	 */
	public int getTurn(final String[] names) {
		for(int i=0; i<names.length; ++i){
			if(names[i].equalsIgnoreCase(getCurrentPlayer())) {
				return i;
			}
		}
		return -1;
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
		try {
			listener = new ServerSocket(PORT);
			if(serializer == Serializer.JAVA_SERIALIZE)
				acceptor = new DefaultAcceptor(this, listener);
			else if(serializer == Serializer.GOOGLE_PROTOBUF)
				acceptor = new ProtoAcceptor(this, listener);
			controller.addMessageEntry(MessageType.INFO, "Server listening on port " + PORT);
			controller.addMessageEntry(MessageType.INFO, "Game mode is " + mode);
			running = true;
			while(running) {
				try {
					Connection connection = acceptor.accept();
					pool.execute(connection);
				} catch (Exception e) {
					running = false;
				}
			}
		} catch (IOException e) {
			PlatformManager.run(new Runnable() {
				@Override
				public void run() {
					new MessageDialog(controller.getAnchorPane().getScene().getWindow(),
							controller.getServerClient(),
							LocalMessages.getString("ErrTitle"),
							e.getMessage())
					.show();
				}
			});
		}
		this.shutdown();
	}
	
	/**
	 * Sets the flag that holds whether the game is in progress.
	 * @param state a boolean
	 */
	public void gameInProgress(boolean state) {
		gameInProgress = state;
	}
	
	/**
	 * Returns true if the game is in progress.
	 */
	public boolean isGameInProgress() {
		return gameInProgress;
	}
	
	/**
	 * Returns the queue of player order.
	 * @return the queue of players
	 */
	public LinkedList<String> getPlayers() {
		return queue;
	}
	
	/**
	 * Returns the current player's name.
	 * @return the player name
	 */
	public String getCurrentPlayer() {
		return currPlayer = queue.getFirst();
	}
	
	/**
	 * Returns the previous player's name.
	 * @return the player name
	 */
	public String getPreviousPlayer() {
		return currPlayer = queue.getLast();
	}
	
	/**
	 * Returns true if the given player name is the current player. (used for SYNC mode; ASYNC mode not applicable)
	 * @param player the player
	 * @return a boolean
	 */
	public boolean isCurrentPlayer(final String player) {
		return player.equalsIgnoreCase(currPlayer);
	}
	
	/**
	 * Sets the next player.
	 */
	public void setNextPlayer() {
		String temp = queue.getFirst();
		if(!currPlayer.equalsIgnoreCase(temp))
			return;
		String name = queue.removeFirst();
		queue.addLast(name);
	}
	
	/**
	 * Adds the player.
	 * @param name the player name
	 */
	public void addPlayer(final String name) {
		players.add(name);
		queue.addLast(name);
	}
	
	/**
	 * Removes the player.
	 * @param name the player name
	 */
	public void removePlayer(final String name) {
		players.remove(name);
		queue.remove(name);
	}
	
	@Override
	public void shutdown() {
		for(Connection connection : connections.values())
			connection.close();
		try {
			if(listener != null)
				listener.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		controller.addMessageEntry(MessageType.INFO, "Server is now offline");
		pool.shutdownNow();
	}
}
