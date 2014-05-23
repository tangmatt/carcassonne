package edu.carleton.comp4905.carcassonne.client.handlers;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PlayerViewHandler implements EventHandler<MouseEvent> {
	private final ImageView playerView;
	private final PopOver popOver;
	private final Node node;
	
	public PlayerViewHandler(final ImageView playerView, final PopOver popOver, final Node node) {
		this.playerView = playerView;
		this.popOver = popOver;
		this.node = node;
	}
	
	@Override
	public void handle(final MouseEvent event) {
		double x = playerView.getX()+playerView.getFitWidth()/2;
		double y = playerView.getY()+playerView.getFitHeight();
		Point2D p = playerView.localToScreen(x, y);
		popOver.setContentNode(node);
		if(!popOver.isDetached())
			popOver.hide();
		popOver.show(playerView, p.getX(), p.getY());
	}
}
