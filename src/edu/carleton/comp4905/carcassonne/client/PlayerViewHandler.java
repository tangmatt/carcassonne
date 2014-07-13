package edu.carleton.comp4905.carcassonne.client;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class PlayerViewHandler implements EventHandler<MouseEvent> {
	private final ImageView playerView;
	private final PopOver popOver;
	private final Node nameNode, scoreNode;
	
	public PlayerViewHandler(final ImageView playerView, final PopOver popOver, final Node nameNode, final Node scoreNode) {
		this.playerView = playerView;
		this.popOver = popOver;
		this.nameNode = nameNode;
		this.scoreNode = scoreNode;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		double x = playerView.getX()+playerView.getFitWidth()/2;
		double y = playerView.getY()+playerView.getFitHeight();
		Point2D p = playerView.localToScreen(x, y);
		VBox vbox = new VBox();
		vbox.getChildren().addAll(nameNode, scoreNode);
		popOver.setContentNode(vbox);
		if(!popOver.isDetached())
			popOver.hide();
		popOver.show(playerView, p.getX(), p.getY());
	}
}
