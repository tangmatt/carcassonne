package edu.carleton.comp4905.carcassonne.server;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import edu.carleton.comp4905.carcassonne.common.Message;
import edu.carleton.comp4905.carcassonne.common.MessageType;
import edu.carleton.comp4905.carcassonne.common.Player;
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
				
				playerView.setPlaceholder(new Text("There are no players connected."));
				logView.setPlaceholder(new Text("There are no log messages."));
			}
		});
	}
	
	/**
	 * Adds a player to the player list.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 */
	public void addPlayerEntry(final String name, final String address, final String port) {
		playerData.add(new Player.PlayerBuilder(name, address, port).build());
	}
	
	/**
	 * Removes a specified player from the player list.
	 * @param name a name (String)
	 * @param address an address (String)
	 * @param port a port (String)
	 */
	public void removePlayerEntry(final String name, final String address, final String port) {
		Player temp = new Player.PlayerBuilder(name, address, port).build();
		Iterator<Player> it = playerData.iterator();
		while(it.hasNext()) {
			Player p = it.next();
			if(temp.equals(p))
				it.remove();
		}
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
	 * Initializes data for this controller.
	 * @param client a ServerClient
	 */
	public void initData(final ServerClient client) {
		this.client = client;
	}
}
