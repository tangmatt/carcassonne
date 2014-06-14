package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import edu.carleton.comp4905.carcassonne.client.handlers.PlayerViewHandler;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class GameController implements Initializable {
	@FXML private BorderPane rootPane;
	@FXML private GridPane gridPane;
	@FXML private HBox previewPane;
	@FXML private Button endTurnButton;
	@FXML private ImageView player1, player2, player3, player4, player5;
	@FXML private ImageView meeple1, meeple2, meeple3, meeple4, meeple5, meeple6, meeple7; // meeple = follower
	private TileDeck deck;
	private TileManager tileManager;
	private GameData gameData;
	private ScoreData scoreData;
	private MouseEvent mousePressEvent;
	private LobbyDialog lobbyDialog;
	private GameClient client;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		mousePressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
		deck = new TileDeck();
		gameData = new GameData();
		scoreData = new ScoreData();
		tileManager = TileManager.getInstance();
		
		ImageView[] playerViews = new ImageView[] { player1, player2, player3, player4, player5 };
		ImageView[] followerViews = new ImageView[] { meeple1, meeple2, meeple3, meeple4, meeple5, meeple6, meeple7 };
		gameData.addPlayerViews(playerViews, createPopOver());
		gameData.addFollowerViews(followerViews);
		setPreviewTiles(tileManager.getEmptyTile()); // initializes the preview tiles to be empty
		
		createGameBoard();
		createLobby();
	}
	
	/**
	 * Adds a TileContainer object to specified row and column of layout.
	 * @param container a TileContainer
	 */
	public synchronized void addTile(final TileContainer container) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				gameData.setTile(container);
				gridPane.add(container, container.c, container.r);
			}
		});
	}
	
	/**
	 * Places tiles onto the tile preview displays.
	 * @param tile a GameTile
	 */
	public synchronized void setPreviewTiles(final GameTile tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				setRotatedPreviews(tile);
				if(previewPane.getChildren().isEmpty())
					previewPane.getChildren().addAll(gameData.getPreviews());
				firePreviewTileEvent(false);
			}
		});
	}
	
	/**
	 * Determines whether the chosen tile can be placed adjacent to existing tiles.
	 * @param preview a TilePreview
	 */
	public synchronized void handleHints(final TilePreview preview) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				refresh();
				for(int c=0; c<GameData.COLS; ++c) {
					for(int r=0; r<GameData.ROWS; ++r) {
						showHint(preview, gameData.getTile(r, c), r, c);
					}
				}
			}
		});
	}
	
	/**
	 * Manages the empty tile adjacent to an existing tile if there is a match.
	 */
	private synchronized void showHint(final TilePreview preview, final TileContainer currTile, final int r, final int c) {
		int tileMatches = 0, tileCount = 0;
		
		if(currTile.isEmpty()) {
			// count non-empty tiles
			tileCount += isPlayableTile(r+1, c);
			tileCount += isPlayableTile(r-1, c);
			tileCount += isPlayableTile(r, c-1);
			tileCount += isPlayableTile(r, c+1);
			
			if(tileCount == 0) // return if there are only empty tiles around the current tile
				return; 
			
			if(matchesTopSegment(preview, r, c))
				tileMatches++;
			if(matchesRightSegment(preview, r, c))
				tileMatches++;
			if(matchesBottomSegment(preview, r, c))
				tileMatches++;
			if(matchesLeftSegment(preview, r, c))
				tileMatches++;
			
			if(tileMatches == tileCount) { // current tile must be matching with all the placed surrounding tiles
				TileContainer selected = gameData.getTile(r, c);
				selected.setSelected(true);
				selected.setHoverTile(true);
				selected.addMouseListener(this,
						new TileContainerHandler(this, r, c),
						new TileHoverHandler(this),
						new TileExitHandler());
			}
		}
	}
	
	/**
	 * Returns a one if the tile at the given row and column contains a valid tile
	 * @param r the row
	 * @param c the column
	 * @return integer
	 */
	private int isPlayableTile(final int r, final int c) {
		int count = 0;
		try {
			count = (gameData.getTile(r, c).isEmpty() || gameData.getTile(r, c) == null) ? 0 : 1;
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return count;
	}
	
	/**
	 * Returns true if preview tile's bottom segment matches the top segment of the tile below.
	 * @param preview the preview tile
	 * @param r the row
	 * @param c the column
	 * @return boolean
	 */
	private boolean matchesBottomSegment(final TilePreview preview, final int r, final int c) {
		boolean match = false;
		try {
			match = preview.matchesBottomSegment(gameData.getTile(r+1, c).getTopSegment());
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return match;
	}
	
	/**
	 * Returns true if preview tile's top segment matches the bottom segment of the tile above.
	 * @param preview the preview tile
	 * @param r the row
	 * @param c the column
	 * @return boolean
	 */
	private boolean matchesTopSegment(final TilePreview preview, final int r, final int c) {
		boolean match = false;
		try {
			match = preview.matchesTopSegment(gameData.getTile(r-1, c).getBottomSegment());
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return match;
	}
	
	/**
	 * Returns true if preview tile's left segment matches the right segment of the tile left.
	 * @param preview the preview tile
	 * @param r the row
	 * @param c the column
	 * @return boolean
	 */
	private boolean matchesLeftSegment(final TilePreview preview, final int r, final int c) {
		boolean match = false;
		try {
			match = preview.matchesLeftSegment(gameData.getTile(r, c-1).getRightSegment());
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return match;
	}
	
	/**
	 * Returns true if preview tile's right segment matches the left segment of the tile right.
	 * @param preview the preview tile
	 * @param r the row
	 * @param c the column
	 * @return boolean
	 */
	private boolean matchesRightSegment(final TilePreview preview, final int r, final int c) {
		boolean match = false;
		try {
			match = preview.matchesRightSegment(gameData.getTile(r, c+1).getLeftSegment());
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		return match;
	}
	
	/**
	 * Initializes the start of a game.
	 */
	public synchronized void startGame() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				deck.shuffle();
				startTurn();
			}
		});
	}
	
	/**
	 * Handles the start of the turn for the current client.
	 */
	public synchronized void startTurn() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				GameTile tile = deck.draw();
				//if(deck.isEmpty())
				// TODO do what after deck is empty??
				setPreviewTiles(tile);
			}
		});
	}
	
	/**
	 * Handles the end of the turn for current client.
	 */
	public synchronized void endTurn() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : gameData.getPreviews())
					preview.setSelected(false);
				for(int c=0; c<GameData.COLS; ++c) {
					for(int r=0; r<GameData.ROWS; ++r) {
						gameData.getTile(r, c).setSelected(false);
						gameData.getTile(r, c).removeMouseListener();
					}
				}
			}
		});
	}
	
	/**
	 * Sets previews to be given tile and its rotated views and adds mouse listener to it.
	 * @param tile a GameTile
	 */
	private void setRotatedPreviews(final GameTile tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				TilePreview[] previews = gameData.getPreviews();
				for(int i=0, degrees=0; i<previews.length; ++i, degrees+=90) {
					boolean original = (degrees == 0);
					if(previews[i] == null)
						previews[i] = new TilePreview(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					else
						previews[i].addTile(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					if(!previews[i].getTile().getName().equals(LocalMessages.getString("EmptyTile")))
						previews[i].addMouseListener(GameController.this, new TilePreviewHandler(GameController.this));
				}
			}
		});
	}
	
	/**
	 * Refreshes the preview and game tiles.
	 */
	public synchronized void refresh() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				refreshPreviews();
				refreshGameTiles();
			}
		});
	}
	
	/**
	 * Refreshes the preview tiles.
	 */
	public synchronized void refreshPreviews() {
		for(TilePreview preview : gameData.getPreviews())
			preview.setSelected(false);
	}
	
	/**
	 * Refreshes all of the game tiles.
	 */
	public synchronized void refreshGameTiles() {
		for(int c=0; c<GameData.COLS; ++c) {
			for(int r=0; r<GameData.ROWS; ++r) {
				TileContainer container = gameData.getTile(r, c);
				container.setEffect(null);
				container.setSelected(false);
				container.removeMouseListener();
				
				if(container.getHoverHandle() != null) {
					container.getChildren().clear();
					container.addTile(tileManager.getEmptyTile());
				}
			}
		}
	}
	
	/**
	 * Fires the event to handle mouse presses on preview tiles.
	 * @param keepState a boolean
	 */
	public synchronized void firePreviewTileEvent(boolean keepState) {
		if(keepState)
			Event.fireEvent(gameData.getSelectedPreviewTile(), mousePressEvent);
		else
			Event.fireEvent(gameData.getPreviews()[0], mousePressEvent);
	}
	
	/**
	 * Creates a PopOver component.
	 */
	private PopOver createPopOver() {
		PopOver popOver = new PopOver();
		popOver.setArrowSize(10f);
		popOver.setArrowIndent(15f);
		popOver.setCornerRadius(0f);
		popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
		popOver.setDetachable(false);
		popOver.setAutoHide(true);
		return popOver;
	}
	
	/**
	 * Creates a dialog for displaying the player queue and pre-game interaction.
	 */
	private void createLobby() {
		PlatformManager.runLater(new Runnable() { // Note: must use runLater otherwise LoadException
			@Override
			public void run() {
				lobbyDialog = new LobbyDialog(gridPane.getScene().getWindow(), client);
			}
		});
	}
	
	/**
	 * Shows the lobby dialog.
	 */
	public synchronized void showLobbyDialog() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				blurGame(true);
				lobbyDialog.show();
			}		
		});
	}
	
	/**
	 * Initializes the game board with empty tiles and the starting tile at the center.
	 */
	private void createGameBoard() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int c=0; c<GameData.COLS; ++c) {
					for(int r=0; r<GameData.ROWS; ++r) {
						TileContainer container;
						if(r == GameData.CENTER_ROW && c == GameData.CENTER_COL)
							container = new TileContainer(tileManager.getStarterTile(), r, c);
						else
							container = new TileContainer(tileManager.getEmptyTile(), r, c);
						addTile(container);
					}
				}
			}
		});
	}
	
	/**
	 * Shows the panel with players information.
	 * @param names an array of Strings
	 * @param statuses an array of booleans
	 */
	public synchronized void updatePlayerPanel(final String[] names, final boolean[] statuses) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Map<ImageView, PopOver> players = gameData.getPlayerViews();
				int i=0;
				for(ImageView imageView : players.keySet()) {
					if(i >= statuses.length) // only loop through the number of connected players
						break;
					
					PopOver popOver = players.get(imageView);
					
					// create node to place into PopOver content
					Label nameLabel = new Label(names[i]);
					nameLabel.setPadding(new Insets(5, 5, 2, 5));
					Label scoreLabel = new Label("Score: " + Integer.toString(scoreData.getPlayerScore(names[i])));
					scoreLabel.setPadding(new Insets(2, 5, 5, 5));
					
					// update player icons according to connection status
					Image image = ResourceManager.getImageFromResources(statuses[i] ? "joined.png" : "disconnected.png");
					imageView.setImage(image);
					imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, new PlayerViewHandler(imageView, popOver, nameLabel, scoreLabel));
					i++;
				}
			}
		});
	}
	
	/**
	 * Shows the amount of followers for the player.
	 */
	public synchronized void updateFollowerPanel() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				ImageView[] followers = gameData.getFollowerViews();
				for(int i=followers.length-1; i>=0; --i) {
					if(i < (followers.length - gameData.getNumOfFollowers()))
						followers[i].setOpacity(0.2f);
					Image image = ResourceManager.getImageFromResources("meeple" + gameData.getIndex() + ".png");
					followers[i].setImage(image);
				}
			}
		});
	}
	
	/**
	 * Retrieves all of the followers and recalculates the connected segment data.
	 */
	public synchronized void updateFollowers() {
		for(TileContainer container : gameData.getTilesWithFollowers().keySet()) {
			String playerName = gameData.getTilesWithFollowers().get(container);
			updateConnectedSegments(container, container.getFollowerPosition(), container.getSegment(container.getFollowerPosition()), playerName);
		}
	}
	
	/**
	 * Updates the connected segments with the player name if they have a follower connected to the segment.
	 * @param container the container
	 * @param position the position
	 * @param segment the segment
	 * @param name the name
	 */
	public void updateConnectedSegments(final TileContainer container, final Position position, final Segment segment, final String name) {
		Set<TileContainer> traversed = new HashSet<TileContainer>();
		container.getTile().updateSegmentOwners(position, name);
		traversed.add(container);
		updateConnectedSegments(container, segment, position, name, traversed);
		System.out.println("_____");
	}
	
	/**
	 * Updates the connected segments with the player name if they have a follower connected to the segment.
	 * @param container the container
	 * @param segment the segment
	 * @param position the position
	 * @param name the name
	 * @param traversed the list of traversed tiles
	 */
	private void updateConnectedSegments(final TileContainer container, final Segment segment,
			final Position position, final String name, final Set<TileContainer> traversed) {
		int r = container.r, c = container.c;

		if(r-1 >= 0) {
			TileContainer temp = gameData.getTile(r-1, c);
			if(segment == temp.getBottomSegment() && !traversed.contains(temp)
					&& (temp.getTile().getFollowerOwner(Position.BOTTOM) == null || temp.getTile().getFollowerOwner(Position.BOTTOM).isEmpty()
					|| temp.getTile().getFollowerOwner(Position.BOTTOM).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.BOTTOM, name);
				traversed.add(temp);
				updateConnectedSegments(temp, temp.getBottomSegment(), Position.BOTTOM, name, traversed);
			}
		}
		if(r+1 < 9) {
			TileContainer temp = gameData.getTile(r+1, c);
			if(segment == temp.getTopSegment() && !traversed.contains(temp)
					&& (temp.getTile().getFollowerOwner(Position.TOP) == null || temp.getTile().getFollowerOwner(Position.TOP).isEmpty()
					|| temp.getTile().getFollowerOwner(Position.TOP).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.TOP, name);
				traversed.add(temp);
				updateConnectedSegments(temp, temp.getTopSegment(), Position.TOP, name, traversed);
			}
		}
		if(c-1 >= 0) {
			TileContainer temp = gameData.getTile(r, c-1);
			if(segment == temp.getRightSegment() && !traversed.contains(temp)
					&& (temp.getTile().getFollowerOwner(Position.RIGHT) == null || temp.getTile().getFollowerOwner(Position.RIGHT).isEmpty()
					|| temp.getTile().getFollowerOwner(Position.RIGHT).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.RIGHT, name);
				traversed.add(temp);
				updateConnectedSegments(temp, temp.getRightSegment(), Position.RIGHT, name, traversed);
			}
		}
		if(c+1 < 9) {
			TileContainer temp = gameData.getTile(r, c+1);
			if(segment == temp.getLeftSegment() && !traversed.contains(temp)
					&& (temp.getTile().getFollowerOwner(Position.LEFT) == null || temp.getTile().getFollowerOwner(Position.LEFT).isEmpty()
					|| temp.getTile().getFollowerOwner(Position.LEFT).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.LEFT, name);
				traversed.add(temp);
				updateConnectedSegments(temp, temp.getLeftSegment(), Position.LEFT, name, traversed);
			}
		}
	}
	
	/**
	 * Updates the game board regarding the completed segments.
	 */
	public synchronized void updateGameBoard() {
		updateCloister();
	}
	
	/**
	 * Checks for completed cloisters and handles accordingly.
	 */
	private void updateCloister() {
		for(int c=0; c<GameData.COLS; ++c) {
			for(int r=0; r<GameData.ROWS; ++r) {
				TileContainer container = gameData.getTile(r, c);
				if(container.getTile().getSegment(Position.CENTER) == Segment.CLOISTER && container.getFollowerPosition() != null) {
					int count = 0;
					if(r-1 >= 0 && !gameData.getTile(r-1, c).isHoverTile() && !gameData.getTile(r-1, c).isEmpty())
						count++;
					if(r+1 < 9 && !gameData.getTile(r+1, c).isHoverTile() && !gameData.getTile(r+1, c).isEmpty())
						count++;
					if(c-1 >= 0 && !gameData.getTile(r, c-1).isHoverTile() && !gameData.getTile(r, c-1).isEmpty())
						count++;
					if(c+1 < 9 && !gameData.getTile(r, c+1).isHoverTile() && !gameData.getTile(r, c+1).isEmpty())
						count++;
					if(count == 4) {
						container.getChildren().remove(container.follower);
						//addPlayerScore(container.getTile().getFollowerOwner(Position.CENTER), 9);
						// TODO display score
						gameData.increaseNumOfFollowers();
						updateFollowerPanel();
						System.out.println("CLOISTER COMPLETED");
					}
				}
			}
		}
	}
	
	/**
	 * Deducts specified score points from the player.
	 * @param name the player name
	 * @param points the score points
	 */
	public synchronized void addPlayerScore(final String name, final int points) {
		scoreData.addPlayerScore(name, points);
	}
	
	/**
	 * Clear the preview tiles and replace with empty tiles.
	 */
	public synchronized void clearPreviews() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : gameData.getPreviews()) {
					preview.setSelected(false);
					preview.removeMouseListener();
					preview.addTile(tileManager.getEmptyTile());
				}
			}
		});
	}
	
	/**
	 * Adds/removes blur from the interface.
	 * @param state
	 */
	public synchronized void blurGame(final boolean state) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				rootPane.setEffect(state ? new GaussianBlur() : null);
			}
		});
	}
	
	/**
	 * Returns the pane containing the preview tiles. 
	 * @return a HBox
	 */
	public synchronized HBox getPreviewPane() {
		return previewPane;
	}
	
	/**
	 * Returns the pane containing the game tiles.
	 * @return a GridPane
	 */
	public synchronized GridPane getGridPane() {
		return gridPane;
	}
	
	/**
	 * Returns the pane containing all the panels for the game.
	 * @return a BorderPane
	 */
	public synchronized BorderPane getRootPane() {
		return rootPane;
	}
	
	/**
	 * Returns the GameData object.
	 * @return a GameData
	 */
	public synchronized GameData getGameData() {
		return gameData;
	}
	
	/**
	 * Returns the ScoreData object.
	 * @return a ScoreData
	 */
	public synchronized ScoreData getScoreData() {
		return scoreData;
	}
	
	/**
	 * Returns the LobbyController object.
	 * @return a LobbyController
	 */
	public synchronized LobbyController getLobbyController() {
		return lobbyDialog.getController();
	}
	
	/**
	 * Returns the GameClient object.
	 * @return a GameClient;
	 */
	public synchronized GameClient getGameClient() {
		return client;
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a GameClient
	 */
	public synchronized void initData(final GameClient client) {
		this.client = client;
	}
}
