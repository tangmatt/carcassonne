package edu.carleton.comp4905.carcassonne.client;

import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

public class TileContainer extends AbstractTile {
	public static final String defaultStyle = "-fx-border-color: gray;"
		    + "-fx-border-width: 1;"
		    + "-fx-border-style: dashed;";
	protected EventHandler<MouseEvent> hoverHandler, exitHandler;
	protected Position selectedFollower;
	protected int r, c;
	protected Follower follower;
	private boolean isHoverTile;
	
	public TileContainer(final GameTile tile, final int r, final int c) {
		super(tile);
		this.r = r;
		this.c = c;
		this.isHoverTile = false;
	}
	
	/**
	 * Shows the follower based on position and registers hover/exit mouse handlers.
	 * @param position the position
	 * @param index the meeple identifier
	 */
	public void showFollower(final Position position, final int index) {
		Follower follower = addFollower(position, index, null);
		follower.setOpacity(0.6f);
		
		follower.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				follower.setOpacity(1f);
				TileContainer.this.setFollower(position, "");
			}
		});
		
		follower.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				follower.setOpacity(0.6f);
				TileContainer.this.setFollower(null, "");
			}
		});
	}
	
	/**
	 * Adds a follower mapped to a side.
	 * @param position the position
	 * @param index the meeple index
	 * @param name the player name
	 */
	public Follower addFollower(final Position position, final int index, final String name) {
		follower = new Follower(position);
		follower.setImage(ResourceManager.getImageFromResources("meeple" + index + ".png"));
		
		if(position == Position.TOP)
			follower.setTranslateY(getTopPositionY(tile, follower));
		else if(position == Position.RIGHT)
			follower.setTranslateX(getRightPositionX(tile, follower));
		else if(position == Position.BOTTOM)
			follower.setTranslateY(getBottomPositionY(tile, follower));
		else if(position == Position.LEFT)
			follower.setTranslateX(getLeftPositionX(tile, follower));
		else if(position == Position.TOP_RIGHT) {
			follower.setTranslateY(getTopPositionY(tile, follower));
			follower.setTranslateX(getRightPositionX(tile, follower));
		}
		else if(position == Position.BOTTOM_RIGHT) {
			follower.setTranslateY(getBottomPositionY(tile, follower));
			follower.setTranslateX(getRightPositionX(tile, follower));
		}
		else if(position == Position.TOP_LEFT) {
			follower.setTranslateY(getTopPositionY(tile, follower));
			follower.setTranslateX(getLeftPositionX(tile, follower));
		}
		else if(position == Position.BOTTOM_LEFT) {
			follower.setTranslateY(getBottomPositionY(tile, follower));
			follower.setTranslateX(getLeftPositionX(tile, follower));
		}
		
		getChildren().add(follower);
		return follower;
	}
	
	public void addMouseListener(final GameController controller,
			final EventHandler<MouseEvent> pressHandler, final EventHandler<MouseEvent> hoverHandler, final EventHandler<MouseEvent> exitHandler) {
		removeMouseListener();
		super.addMouseListener(controller, pressHandler);
		this.hoverHandler = hoverHandler;
		this.exitHandler = exitHandler;
		if(hoverHandler != null)
			addEventHandler(MouseEvent.MOUSE_ENTERED, this.hoverHandler);
		if(exitHandler != null)
			addEventHandler(MouseEvent.MOUSE_EXITED, this.exitHandler);
	}
	
	@Override
	public void removeMouseListener() {
		super.removeMouseListener();
		if(hoverHandler != null)
			removeEventHandler(MouseEvent.MOUSE_ENTERED, hoverHandler);
		if(exitHandler != null)
			removeEventHandler(MouseEvent.MOUSE_EXITED, exitHandler);
	}
	
	/**
	 * Returns the amount of pixels to move the follower tile from the center to the right side.
	 * @param gameTile a GameTile
	 * @param followerTile a Follower
	 * @return a double
	 */
	public double getRightPositionX(final GameTile gameTile, final Follower followerTile) {
		return gameTile.getImage().getWidth()/2f - followerTile.getImage().getWidth()/2f - 1;
	}
	
	/**
	 * Returns the amount of pixels to move the follower tile from the center to the left side.
	 * @param gameTile a GameTile
	 * @param followerTile a GameTile
	 * @return a double
	 */
	public double getLeftPositionX(final GameTile gameTile, final Follower followerTile) {
		return (-1) * getRightPositionX(gameTile, followerTile);
	}
	
	/**
	 * Returns the amount of pixels to move the follower tile from the center to the bottom side.
	 * @param gameTile a GameTile
	 * @param followerTile a GameTile
	 * @return a double
	 */
	public double getBottomPositionY(final GameTile gameTile, final Follower followerTile) {
		return gameTile.getImage().getHeight()/2f - followerTile.getImage().getHeight()/2f - 1;
	}
	
	/**
	 * Returns the amount of pixels to move the follower tile from the center to the top side.
	 * @param gameTile a GameTile
	 * @param followerTile a GameTile
	 * @return a double
	 */
	public double getTopPositionY(final GameTile gameTile, final Follower followerTile) {
		return (-1) * getBottomPositionY(gameTile, followerTile);
	}
	
	/**
	 * Sets the follower position and owner.
	 * @param position the follower position
	 * @param name the player name
	 */
	public void setFollower(final Position position, final String name) {
		selectedFollower = position;
		tile.setPosition(position, name);
	}
	
	/**
	 * Returns the follower position.
	 * @return a Position
	 */
	public Position getFollowerPosition() {
		return selectedFollower;
	}
	
	/**
	 * Returns the name of the follower placer.
	 * @param position the position
	 * @return the name of the follower placer
	 */
	public String getFollowerOwner(final Position position) {
		return tile.getFollowerOwner(position);
	}
	
	/**
	 * Returns the mouse hover event handler.
	 * @return the EventHandler
	 */
	public EventHandler<MouseEvent> getHoverHandle() {
		return hoverHandler;
	}
	
	/**
	 * Returns the row index.
	 * @return the row
	 */
	public int getRow() {
		return r;
	}
	
	/**
	 * Returns the column index.
	 * @return the column
	 */
	public int getColumn() {
		return c;
	}
	
	/**
	 * Determines whether the tile is a hover tile or not.
	 * @param state the state
	 */
	public void setHoverTile(final boolean state) {
		isHoverTile = state;
	}
	
	/**
	 * Returns true if this tile container is simply a hovered tile that is not permanent to the board.
	 * @return a boolean
	 */
	public boolean isHoverTile() {
		return isHoverTile;
	}
	
	/**
	 * Returns true if container is empty.
	 * @return a boolean
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() || isHoverTile();
	}

	@Override
	public void setSelected(final boolean state) {
		if(state) {
			setCursor(Cursor.HAND);
			setStyle(selectedStyle);
		} else {
			setCursor(Cursor.DEFAULT);
			setStyle(defaultStyle);
		}
	}
}