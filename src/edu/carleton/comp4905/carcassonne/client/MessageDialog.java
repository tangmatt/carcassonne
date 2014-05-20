package edu.carleton.comp4905.carcassonne.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import edu.carleton.comp4905.carcassonne.common.Dialog;

public class MessageDialog extends Dialog {

	public MessageDialog(Window window, Application client, String title, String message) {
		super(window, client, title, message);
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				((GameClient)client).getController().blurGame(false);
				((GameClient)client).getStage().getOnCloseRequest().handle(null);
				close();
			}
		});
	}
}