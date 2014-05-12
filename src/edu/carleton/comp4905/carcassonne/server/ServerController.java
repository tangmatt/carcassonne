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
import javafx.scene.text.Text;

public class ServerController implements Initializable {
	@FXML private TableView<Player> playerView;
	@FXML private TableView<Message> logView;
	@FXML private TableColumn<Player, String> playerColumn, addrColumn, portColumn;
	@FXML private TableColumn<Message, String> dateColumn, timeColumn, messageColumn;
	@FXML private TableColumn<Message, MessageType> typeColumn;
	@FXML private TextArea messageDesc;
	private ObservableList<Player> playerData;
	private ObservableList<Message> messageData;
	
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
	
	public void addPlayerEntry(String name, String address, String port) {
		playerData.add(new Player.PlayerBuilder(name, address, port).build());
	}
	
	public void removePlayerEntry(String name, String address, String port) {
		Player temp = new Player.PlayerBuilder(name, address, port).build();
		Iterator<Player> it = playerData.iterator();
		while(it.hasNext()) {
			Player p = it.next();
			if(temp.equals(p))
				it.remove();
		}
	}
	
	public void addMessageEntry(MessageType type, String message) {
		messageData.add(new Message(type, message));
	}
}
