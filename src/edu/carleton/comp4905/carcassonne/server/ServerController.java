package edu.carleton.comp4905.carcassonne.server;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Address;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Message;
import edu.carleton.comp4905.carcassonne.common.MessageType;
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
	
	public ServerController() {
		playerData = FXCollections.observableArrayList();
		messageData = FXCollections.observableArrayList();
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
				
				// Row selection listener; displays log message in text area
				logView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
					Message selectedMessage = (Message)newValue;
					if(selectedMessage != null)
						messageDesc.setText(selectedMessage.getMessage());
				});
				
				playerView.setPlaceholder(new Text("There are no players in the game."));
				logView.setPlaceholder(new Text("There are no log messages."));
			}
		});
	}
	
	/**
	 * Changes the status of the specified player to indicate connection.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 */
	public void connectPlayer(final String name, final String address, final String port) {
		playerData.add(new Player.PlayerBuilder(name, address, port, Status.CONNECTED).build());
	}
	
	/**
	 * Changes the status of the specified player to indicate connectivity.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 * @param status a Status
	 * @return boolean
	 */
	public boolean updatePlayer(final String name, final String address, final String port, final Status status) {
		Player temp = new Player.PlayerBuilder(name, address, port, status).build();
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
	public void removePlayer(final String name, final String address, final String port, final Status status) {
		Player temp = new Player.PlayerBuilder(name, address, port, status).build();
		Iterator<Player> it = playerData.iterator();
		while(it.hasNext()) {
			Player p = it.next();
			if(temp.equals(p))
				it.remove();
		}
	}
	
	/**
	 * Removes the connection mapped to the address and port.
	 * @param connections a Map<Address, Connection>
	 * @param address a String
	 * @param port an Integer
	 */
	public void removeConnection(final Map<Address, Connection> connections, final String address, final int port) {
		Iterator<Map.Entry<Address, Connection>> it = connections.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Address, Connection> pairs = (Map.Entry<Address, Connection>)it.next();
			Address temp = (Address)pairs.getKey();
			if(temp.equals(new Address(address, port)))
				it.remove();
		}
	}
	
	/**
	 * Returns the players' status.
	 * @return an array of booleans
	 */
	public boolean[] getStatuses(final Map<Address, Connection> connections) {
		boolean[] statuses = new boolean[playerData.size()];
		for(int i=0; i<statuses.length; ++i)
			statuses[i] = playerData.get(i).isConnected();
		return statuses;
	}
	
	/**
	 * Returns all players' names.
	 * @return an array of Strings
	 */
	public String[] getPlayerNames() {
		String[] names = new String[playerData.size()];
		for(int i=0; i<names.length; ++i)
			names[i] = playerData.get(i).getName();
		return names;
	}
	
	/**
	 * Returns the remaining player (assumes only one player is connected)
	 * @return Player
	 */
	public Player getRemainingPlayer() {
		for(Player player : playerData) {
			if(player.isConnected()) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Handles when the game is over.
	 */
	public void handleGameFinish() {
		playerData.clear();
	}
	
	/**
	 * Adds a message to the log messages list.
	 * @param type a MessageType
	 * @param message a message (String)
	 */
	public void addMessageEntry(final MessageType type, final String message) {
		messageData.add(new Message(type, message));
	}
	
	/**
	 * Returns the AnchorPane object.
	 * @return an AnchorPane
	 */
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}
	
	/**
	 * Returns the ServerClient object.
	 * @return a ServerClient
	 */
	public ServerClient getServerClient() {
		return client;
	}
	
	/**
	 * Returns a list of players.
	 * @return an ObservableList
	 */
	public ObservableList<Player> getPlayers() {
		return playerData;
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a ServerClient
	 */
	public void initData(final ServerClient client) {
		this.client = client;
	}
}
