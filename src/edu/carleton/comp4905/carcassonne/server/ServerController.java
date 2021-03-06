package edu.carleton.comp4905.carcassonne.server;

import java.io.IOException;
import java.net.URL;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Message;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.Player;
import edu.carleton.comp4905.carcassonne.common.Player.Status;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ServerController implements Initializable {
	@FXML private AnchorPane anchorPane;
	@FXML private TableView<Player> playerView;
	@FXML private TableView<Message> logView;
	@FXML private TableColumn<Player, String> playerColumn, addrColumn, portColumn;
	@FXML private TableColumn<Player, Status> statusColumn;
	@FXML private TableColumn<Message, String> dateColumn, timeColumn, messageColumn;
	@FXML private TableColumn<Message, MessageType> typeColumn;
	@FXML private TextArea messageDesc;
	private final ObservableList<Player> playerData;
	private final ObservableList<Message> messageData;
	private ServerClient client;
	private TileDeck deck;
	
	public ServerController() {
		playerData = FXCollections.observableArrayList();
		messageData = FXCollections.observableArrayList();
		deck = new TileDeck();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logView.setItems(messageData);
				playerView.setItems(playerData);
				
				playerColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
				addrColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("address"));
				portColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("port"));
				statusColumn.setCellValueFactory(new PropertyValueFactory<Player, Status>("status"));
				dateColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("date"));
				timeColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("time"));
				typeColumn.setCellValueFactory(new PropertyValueFactory<Message, MessageType>("type"));
				messageColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
				
				// row selection listener; displays log message in text area
				logView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
					Message selectedMessage = newValue;
					if(selectedMessage != null)
						messageDesc.setText(selectedMessage.getMessage());
				});
				
				// auto scroll to bottom of log viewer (commented out because of warning messages after exit)
				/*logView.getItems().addListener(new ListChangeListener<Object>() {
					@Override
					public void onChanged(ListChangeListener.Change<? extends Object> change) {
						logView.scrollTo(logView.getItems().size()-1);
					}
				});*/
				
				playerView.setPlaceholder(new Text(LocalMessages.getString("NoPlayersMessage")));
				logView.setPlaceholder(new Text(LocalMessages.getString("NoLogMessage")));
			}
		});
	}
	
	/**
	 * Changes the status of the specified player to indicate connection.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 */
	public synchronized void connectPlayer(final String name, final String address, final String port) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				playerData.add(new Player.PlayerBuilder(name, address, port, Status.CONNECTED, false).build());
			}
		});
	}
	
	/**
	 * Changes the status of the specified player to indicate connectivity.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 * @param status a Status
	 * @return boolean
	 */
	public synchronized boolean updatePlayer(final String name, final String address, final String port, final Status status) {
		Player temp = new Player.PlayerBuilder(name, address, port, status, false).build();
		int index = 0;
		Iterator<Player> it = playerData.iterator();
		while(it.hasNext()) { // replace matching player with updated status
			Player p = it.next();
			if(temp.equals(p)) {
				playerData.set(index, temp);
				return true;
			}
			index++;
		}
		return false;
	}
	
	/**
	 * Removes the specified player from data list.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 * @return boolean
	 */
	public synchronized void removePlayer(final String name, final String address, final String port, final Status status) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Player temp = new Player.PlayerBuilder(name, address, port, status, false).build();
				Iterator<Player> it = playerData.iterator();
				while(it.hasNext()) {
					Player p = it.next();
					if(temp.equals(p))
						it.remove();
				}
			}
		});
	}
	
	/**
	 * Removes the connection mapped to the address and port.
	 * @param connections a Map<Address, Connection>
	 * @param address a String
	 * @param port an Integer
	 */
	public synchronized void removeConnection(final Map<Address, Connection> connections, final String address, final int port) {
		Iterator<Map.Entry<Address, Connection>> it = connections.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Address, Connection> pairs = it.next();
			Address temp = pairs.getKey();
			if(temp.equals(new Address(address, port))) {
				it.remove();
			}
		}
	}
	
	/**
	 * Returns the players' status.
	 * @return an array of statuses
	 */
	public synchronized Player.Status[] getStatuses(final Map<Address, Connection> connections) {
		Player.Status[] statuses = new Player.Status[playerData.size()];
		for(int i=0; i<statuses.length; ++i) {
			Player player = playerData.get(i);
			if(player.isConnected()) {
				if(getServerClient().getServer().isCurrentPlayer(player.getName()))
					statuses[i] = Player.Status.PLAYING;
				else
					statuses[i] = Player.Status.CONNECTED;
			} else {
				statuses[i] = Player.Status.DISCONNECTED;
			}
		}
		return statuses;
	}
	
	/**
	 * Returns all players' names.
	 * @return an array of Strings
	 */
	public synchronized String[] getPlayerNames() {
		String[] names = new String[playerData.size()];
		for(int i=0; i<names.length; ++i)
			names[i] = playerData.get(i).getName();
		return names;
	}
	
	/**
	 * Returns the number of players connected
	 * @return the number of players connected
	 */
	public synchronized int getNumberOfPlayersConnected() {
		int total = 0;
		for(Player player : playerData) {
			if(player.isConnected())
				total++;
		}
		return total;
	}
	
	/**
	 * Returns the remaining player (assumes only one player is connected)
	 * @return Player
	 */
	public synchronized Player getRemainingPlayer() {
		for(Player player : playerData) {
			if(player.isConnected()) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if there is already a player with the same name.
	 * @param playerName a String
	 * @return a boolean
	 */
	public synchronized boolean playerExists(final String playerName) {
		for(Player player : playerData) {
			if(player.getName().equalsIgnoreCase(playerName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the players' index in the list.
	 * @return an array of String
	 */
	public synchronized String[] getPlayerIndices() {
		String[] list = new String[playerData.size()];
		int index = 0;
		for(Player player : playerData) {
			list[index++] = player.getName();
		}
		return list;
	}
	
	/**
	 * Updates whether the player has a tile in their hand.
	 * @param playerName the player name
	 * @param hasTile true if player has tile
	 */
	public synchronized void updatePlayerHand(final String playerName, final boolean hasTile) {
		for(Player player : playerData) {
			if(player.getName().equals(playerName)) {
				player.setHasTile(hasTile);
				break;
			}
		}
	}
	
	/**
	 * Returns true if all players have no tile at hand.
	 * @return true if all players have no tile at hand
	 */
	public synchronized boolean allPlayersHaveNoTile() {
		for(Player player : playerData) {
			if(player.isConnected() && player.hasTile())
				return false;
		}
		return true;
	}
	
	/**
	 * Handles when the game is over.
	 */
	public synchronized void handleGameFinish() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				playerData.clear();
			}
		});
	}
	
	/**
	 * Adds to the player's score.
	 * @param name the player name
	 */
	public synchronized void addPlayerScore(final String name, int points) {
		for(Player player : playerData) {
			if(player.getName().equalsIgnoreCase(name)) {
				player.addPoints(points);
				break;
			}
		}
	}
	
	/**
	 * Adds a message to the log messages list.
	 * @param type a MessageType
	 * @param message a message (String)
	 */
	public synchronized void addMessageEntry(final MessageType type, final String message) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				messageData.add(new Message(type, message));
			}
		});
	}
	
	/**
	 * Returns a list of winner(s).
	 * @return list of winner(s)
	 */
	public synchronized List<String> getWinners() {
		int highest = Integer.MIN_VALUE;
		List<String> winners = new ArrayList<String>();
		// get highest number of points
		for(Player player : playerData) {
			if(player.getScore() > highest)
				highest = player.getScore();
		}
		// check for players with the same high score
		for(Player player : playerData) {
			if(player.getScore() == highest)
				winners.add(player.getName());
		}
		return winners;
	}
	
	/**
	 * Returns a message announcing the winner(s).
	 * @return a message
	 */
	public synchronized String getWinnerMessage() {
		double[] fileLimits = {1, 2};
		String[] fileStrings = {
			LocalMessages.getString("OneWinner"),
			LocalMessages.getString("MultipleWinners")
		};
		
		MessageFormat messageForm = new MessageFormat("");
		ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);
		String pattern = LocalMessages.getString("WinnerInfo");
		Format[] formats = {choiceForm, null, NumberFormat.getInstance()};
		messageForm.applyPattern(pattern);
		messageForm.setFormats(formats);
		
		List<String> winners = getWinners();
		String parsed = winners.toString().substring(1,  winners.toString().length());
		parsed = parsed.substring(0, parsed.length()-1);
		Object[] messageArguments = {winners.size(), parsed};
		String result = messageForm.format(messageArguments);
		return result;
	}
	
	/**
	 * Returns the player's score.
	 * @param name the player name
	 * @return a score
	 */
	public synchronized int getPlayerScore(final String name) {
		for(Player player : playerData) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player.getScore();
			}
		}
		return -1;
	}
	
	/**
	 * Sends event to client.
	 * @param event an event
	 * @param connection the connection
	 */
	public void sendEvent(final Event event, final Connection connection) {
		try {
			connection.sendEvent(event);
		} catch (IOException e) {
			addMessageEntry(MessageType.ERROR, LocalMessages.getString("UnableToSendReply"));
		}
	}
	
	/**
	 * Sends event to all clients.
	 * @param event an event
	 * @param connection the connection
	 * @param connections the connections
	 */
	public void broadcastEvent(final Event event, final Connection connection, final ConcurrentMap<Address, Connection> connections) {
		try {
			connection.broadcastEvent(event, connections);
		} catch (IOException e) {
			addMessageEntry(MessageType.ERROR, LocalMessages.getString("UnableToSendReply"));
		}
	}
	
	/**
	 * Closes the server application.
	 */
	public synchronized void closeServerApplication() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				client.getStage().getOnCloseRequest().handle(null);
			}
		});
	}
	
	/**
	 * Returns the AnchorPane object.
	 * @return an AnchorPane
	 */
	public synchronized AnchorPane getAnchorPane() {
		return anchorPane;
	}
	
	/**
	 * Returns the ServerClient object.
	 * @return a ServerClient
	 */
	public synchronized ServerClient getServerClient() {
		return client;
	}
	
	/**
	 * Returns a list of players.
	 * @return an ObservableList
	 */
	public synchronized ObservableList<Player> getPlayers() {
		return playerData;
	}
	
	/**
	 * Returns the deck.
	 * @return the deck
	 */
	public synchronized TileDeck getDeck() {
		return deck;
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a ServerClient
	 */
	public synchronized void initData(final ServerClient client) {
		this.client = client;
	}
}
