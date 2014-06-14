package edu.carleton.comp4905.carcassonne.server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import edu.carleton.comp4905.carcassonne.common.Dialog;

public class MessageDialog extends Dialog {

	public MessageDialog(final Window window, final Application client, final String title, final String message) {
		super(window, client, title, message);
		
		setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				((ServerClient)client).getStage().getOnCloseRequest().handle(null);
				close();
			}
		});
	}
}
