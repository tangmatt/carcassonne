package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import edu.carleton.comp4905.carcassonne.common.CommonUtil;
import edu.carleton.comp4905.carcassonne.common.Connection;
import edu.carleton.comp4905.carcassonne.common.Event;
import edu.carleton.comp4905.carcassonne.common.EventType;
import edu.carleton.comp4905.carcassonne.common.LocalMessages;
import edu.carleton.comp4905.carcassonne.common.Mode;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.Player;
import edu.carleton.comp4905.carcassonne.common.Position;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import edu.carleton.comp4905.carcassonne.common.TileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
	@FXML private ScrollPane scrollPane;
	@FXML private GridPane gridPane;
	@FXML private HBox previewPane;
	@FXML private Label deckLabel, scoreLabel, pointsLabel;
	@FXML private ImageView player1, player2, player3, player4, player5;
	@FXML private ImageView meeple1, meeple2, meeple3, meeple4, meeple5, meeple6, meeple7; // meeple a.k.a follower
	private ImageView[] playerViews;
	private PopOver popOver;
	private TileManager tileManager;
	private GameData gameData;
	private ScoreData scoreData;
	private MouseEvent mousePressEvent;
	private LobbyDialog lobbyDialog;
	private GameClient client;
	private TileContainer lastTile;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		mousePressEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
		gameData = new GameData();
		scoreData = new ScoreData();
		tileManager = TileManager.getInstance();
		
		playerViews = new ImageView[] { player1, player2, player3, player4, player5 };
		ImageView[] followerViews = new ImageView[] { meeple1, meeple2, meeple3, meeple4, meeple5, meeple6, meeple7 };
		popOver = createPopOver();
		gameData.addFollowerViews(followerViews);
		
		removePreviewTiles();
		createLobby();
	}
	
	/**
	 * Initializes the game data and board.
	 */
	public synchronized void initGame() {
		gameData.init();
		updateGridHeight();
		updateGridWidth();
		createGameBoard();
	}
	
	/**
	 * Adds a TileContainer object to specified row and column of layout and updates the game data.
	 * @param container a TileContainer
	 * @param row the row
	 * @param column the column
	 */
	public synchronized void putTile(final TileContainer container, final int row, final int column) {
		gameData.setTile(container, row, column);
		gridPane.add(container, column, row);
	}
	
	/**
	 * Adds tile to view then handles if boundaries have been triggered (for expansion).
	 * @param container a TileContainer
	 * @param row the row
	 * @param column the column
	 */
	public synchronized void addTile(final TileContainer container, final int row, final int column) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				lastTile = container;
				putTile(container, row, column);
				if(!container.isEmpty()) {
					if(gameData.getColumnIndex(container) == GameData.COLS-1) {
						gameData.expandRightColumn();
						for(int r=0; r<GameData.ROWS; ++r) {
							TileContainer temp = new TileContainer(tileManager.getEmptyTile());
							putTile(temp, r, gameData.getColumnIndex(container)+1);
						}
						updateGridWidth();
					}
					if(gameData.getRowIndex(container) == GameData.ROWS-1) {
						gameData.expandBottomRow();
						for(int c=0; c<GameData.COLS; ++c) {
							TileContainer temp = new TileContainer(tileManager.getEmptyTile());
							putTile(temp, gameData.getRowIndex(container)+1, c);
						}
						updateGridHeight();
					}
					if(gameData.getColumnIndex(container) == 0) {
						gridPane.getChildren().clear();
						gameData.expandLeftColumn();
						updateGameBoard();
						updateGridWidth();
					}
					if(gameData.getRowIndex(container) == 0) {
						gridPane.getChildren().clear();
						gameData.expandTopRow();
						updateGameBoard();
						updateGridHeight();
					}
				}
			}
		});
	}
	
	/**
	 * Updates the game board in regards to the data stored.
	 */
	public synchronized void updateGameBoard() {
		for(int r=0; r<GameData.ROWS; ++r) {
			for(int c=0; c<GameData.COLS; ++c) {
				TileContainer tile = gameData.getTile(r, c);
				putTile(tile, r, c);
			}
		}
	}
	
	/**
	 * Places tiles onto the tile preview displays.
	 * @param tile a GameTile
	 * @param clickFirstTile a boolean to mouse press on first tile
	 */
	public synchronized void setPreviewTiles(final GameTile tile, final boolean clickFirstTile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				setRotatedPreviews(tile);
				if(previewPane.getChildren().isEmpty())
					previewPane.getChildren().addAll(gameData.getPreviews());
				if(clickFirstTile)
					firePreviewTileEvent(false);
			}
		});
	}
	
	/**
	 * Removes the preview tiles.
	 */
	public synchronized void removePreviewTiles() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				stopInteraction(true);
				setPreviewTiles(tileManager.getEmptyTile(), false);
				clearBoard();
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
				for(int r=0; r<GameData.ROWS; ++r) {
					for(int c=0; c<GameData.COLS; ++c) {
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
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				if(isValidTilePlacement(preview, currTile, r, c) == 1) {
					TileContainer selected = gameData.getTile(r, c);
					selected.setSelected(true);
					selected.setHoverTile(true);
					selected.addMouseListener(GameController.this,
							new TileContainerHandler(GameController.this, r, c),
							new TileHoverHandler(GameController.this),
							new TileExitHandler());
				}
			}
		});
	}
	
	/**
	 * Returns a "1" if the specified location is a valid placement.
	 * @param preview the preview tile
	 * @param currTile the current tile
	 * @param r the row
	 * @param c the column
	 * @return a 1 if the specified location is a valid placement
	 */
	private int isValidTilePlacement(final TilePreview preview, final TileContainer currTile, final int r, final int c) {
		int tileMatches = 0, tileCount = 0;
		if(currTile.isEmpty()) {
			// count non-empty tiles
			tileCount += isPlayableTile(r+1, c);
			tileCount += isPlayableTile(r-1, c);
			tileCount += isPlayableTile(r, c-1);
			tileCount += isPlayableTile(r, c+1);
			
			if(tileCount == 0) // return if there are only empty tiles around the current tile
				return 0;

			if(matchesTopSegment(preview, r, c))
				tileMatches++;
			if(matchesRightSegment(preview, r, c))
				tileMatches++;
			if(matchesBottomSegment(preview, r, c))
				tileMatches++;
			if(matchesLeftSegment(preview, r, c))
				tileMatches++;

			if(tileMatches == tileCount) { // current tile must be matching with all the placed surrounding tiles
				return 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * Returns true if the preview tiles cannot be placed
	 * @return a boolean
	 */
	public synchronized boolean isDiscardable() {
		TilePreview[] previews = gameData.getPreviews();
		int sum = 0;
		for(int i=0; i<previews.length; ++i) {
			for(int r=0; r<GameData.ROWS; ++r) {
				for(int c=0; c<GameData.COLS; ++c) {
					sum += isValidTilePlacement(previews[i], gameData.getTile(r, c), r, c);
				}
			}
		}
		return sum == 0;
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
			count = (gameData.getTile(r, c) == null || gameData.getTile(r, c).isEmpty()) ? 0 : 1;
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
			// do nothing
		}
		return match;
	}
	
	/**
	 * Sends the turn request to the server.
	 */
	public void sendTurnRequest() {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.START_TURN_REQUEST, client.getGame().getPlayerName());
		connection.sendEvent(event);
	}
	
	/**
	 * Sends the end game request to the server.
	 * @param title the title
	 * @param message the message
	 */
	public void sendEndGameRequest(final String title, final String message) {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.END_GAME_REQUEST, client.getGame().getPlayerName());
		event.addProperty("messageTitle", title);
		event.addProperty("message", message);
		connection.sendEvent(event);
	}
	
	/**
	 * Sends the end turn request to the server.
	 */
	public void sendEndTurnRequest() {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.END_TURN_REQUEST, client.getGame().getPlayerName());
		connection.sendEvent(event);
	}
	
	/**
	 * Sends the quit request to the server.
	 */
	public void sendQuitRequest() {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.QUIT_REQUEST, client.getGame().getPlayerName());
		connection.sendEvent(event);
	}
	
	/**
	 * Sends the update score request to the server.
	 * @param name the player name
	 * @param points the points earned
	 */
	public void sendScoreUpdateRequest(final String name, final int points) {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.SCORE_UPDATE_REQUEST, client.getGame().getPlayerName());
		event.addProperty("target", name);
		event.addProperty("points", points);
		connection.sendEvent(event);
	}
	
	/**
	 * Sends the update follower request to the server.
	 * @param name the player name
	 * @param followers the follower amount
	 */
	public void sendFollowerUpdateRequest(final String name, final int followers) {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.FOLLOWER_UPDATE_REQUEST, client.getGame().getPlayerName());
		event.addProperty("target", name);
		event.addProperty("followers", followers);
		connection.sendEvent(event);
	}
	
	/**
	 * Handles the start of the turn for the current client.
	 * @param tile the drawn tile
	 */
	public synchronized void startTurn(final String tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				setPreviewTiles(tileManager.getTile(tile), true);
				stopInteraction(false);
				if(isDiscardable())
					sendTurnRequest();
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
				clearBoard();
				stopInteraction(true);
			}
		});
		sendEndTurnRequest();
	}
	
	/**
	 * Handles tile selection.
	 */
	public synchronized void handleTileSelect(final GameTile selectedTile, final TileContainer container, 
			final Connection connection, final int row, final int column) {
		// send tile placement event to server
		Event gameEvent = new Event(EventType.SEND_TILE_REQUEST, client.getGame().getPlayerName());
		gameEvent.addProperty("tile", selectedTile.getName());
		gameEvent.addProperty("rotation", ((Double)selectedTile.getRotate()).intValue());
		gameEvent.addProperty("row", row);
		gameEvent.addProperty("column", column);
		gameEvent.addProperty("meeple", gameData.getIndex());
		gameEvent.addProperty("position", container.getFollowerPosition());
		gameEvent.addProperty("shield", container.getTile().hasShield());
		connection.sendEvent(gameEvent);
		container.setHoverTile(false);
		endTurn();
	}
	
	/**
	 * Clears the board such as the preview tiles and the board.
	 */
	private void clearBoard() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : gameData.getPreviews())
					preview.setSelected(false);
				for(int r=0; r<GameData.ROWS; ++r) {
					for(int c=0; c<GameData.COLS; ++c) {
						if(lastTile != gameData.getTile(r, c))
							gameData.getTile(r, c).setSelected(false);
						gameData.getTile(r, c).removeMouseListener();
					}
				}
			}
		});
	}
	
	/**
	 * Determines whether to disable the preview and grid panel.
	 * @param state the state
	 */
	public synchronized void stopInteraction(final boolean state) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				previewPane.setDisable(state);
				gridPane.setDisable(state);
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
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : gameData.getPreviews())
					preview.setSelected(false);
			}
		});
	}
	
	/**
	 * Refreshes all of the game tiles.
	 */
	public synchronized void refreshGameTiles() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int r=0; r<GameData.ROWS; ++r) {
					for(int c=0; c<GameData.COLS; ++c) {
						TileContainer container = gameData.getTile(r, c);
						container.setEffect(null);
						if(lastTile != container)
							container.setSelected(false);
						container.removeMouseListener();

						if(container.getHoverHandle() != null) {
							container.getChildren().clear();
							container.addTile(tileManager.getEmptyTile());
						}
					}
				}
			}
		});
	}
	
	/**
	 * Fires the event to handle mouse presses on preview tiles.
	 * @param keepState a boolean
	 */
	public synchronized void firePreviewTileEvent(boolean keepState) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				try {
					if(keepState)
						javafx.event.Event.fireEvent(gameData.getSelectedPreviewTile(), mousePressEvent);
					else
						javafx.event.Event.fireEvent(gameData.getPreviews()[0], mousePressEvent);
				} catch(Exception e) {
					// do nothing
				}
			}
		});
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
	public synchronized void createLobby() {
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
	 * Shows the current player's turn on the player panel.
	 * @param index the player index
	 * @param numOfPlayers the number of players
	 */
	public synchronized void showCurrentPlayerTurn(final int index, final int numOfPlayers) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<playerViews.length; ++i) {
					if(i >= numOfPlayers)
						break;
					playerViews[i].setImage(ResourceManager.getImageFromResources((i == index) ? "current.png" : "joined.png"));
				}
			}
		});
	}
	
	/**
	 * Initializes the game board with empty tiles and the starting tile at the center.
	 */
	public synchronized void createGameBoard() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int r=0; r<GameData.ROWS; ++r) {
					for(int c=0; c<GameData.COLS; ++c) {
						addTile(new TileContainer(tileManager.getEmptyTile()), r, c);
					}
				}
			}
		});
	}
	
	/**
	 * Adds the starting tile to the specified coordinates.
	 * @param row the row
	 * @param column the column
	 */
	public synchronized void addStartingTile(final int row, final int column) {
		addTile(new TileContainer(tileManager.getStarterTile()), row, column);
	}
	
	/**
	 * Shows the panel with players information.
	 * @param names an array of Strings
	 * @param statuses an array of booleans
	 * @param mode the mode
	 */
	public synchronized void updatePlayerPanel(final String[] names, final Player.Status[] statuses, final Mode mode) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<names.length; ++i) {
					if(i >= statuses.length) // only loop through the number of connected players
						break;
					
					// create node to place into PopOver content
					Label nameLabel = new Label(names[i]);
					nameLabel.setPadding(new Insets(5, 5, 2, 5));
					Label scoreLabel = new Label("Score: " + scoreData.getPlayerScore(names[i]));
					scoreLabel.setPadding(new Insets(2, 5, 5, 5));
					
					// update player icons according to connection status
					Image image = null;
					if(statuses[i] == Player.Status.CONNECTED)
						image = ResourceManager.getImageFromResources("joined.png");
					else if(statuses[i] == Player.Status.DISCONNECTED)
						image = ResourceManager.getImageFromResources("disconnected.png");
					else if(statuses[i] == Player.Status.PLAYING) {
						if(mode == Mode.SYNC)
							image = ResourceManager.getImageFromResources("current.png");
						else
							image = ResourceManager.getImageFromResources("joined.png");
					}
					playerViews[i].setImage(image);
					playerViews[i].addEventHandler(MouseEvent.MOUSE_ENTERED, new PlayerViewHandler(playerViews[i], popOver, nameLabel, scoreLabel));
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
					followers[i].setOpacity((i < (followers.length - gameData.getNumOfFollowers())) ? 0.2f : 0.6f);
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
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TileContainer container : gameData.getTilesWithFollowers().keySet()) {
					String playerName = gameData.getTilesWithFollowers().get(container);
					updateConnectedSegments(container, container.getFollowerPosition(), playerName);
				}
			}
		});
	}
	
	/**
	 * Updates the connected segments with the player name if they have a follower connected to the segment.
	 * @param container the container
	 * @param position the position
	 * @param name the name
	 */
	public synchronized void updateConnectedSegments(final TileContainer container, final Position position, final String name) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Set<TileContainer> traversed = new HashSet<TileContainer>();
				container.getTile().updateSegmentOwners(position, name);
				traversed.add(container);
				updateConnectedSegments(container, container.getSegment(position), position, name, traversed);
			}
		});
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
		final int r = gameData.getRowIndex(container), c = gameData.getColumnIndex(container);

		if(r-1 >= 0) {
			TileContainer temp = gameData.getTile(r-1, c);
			if(temp != null && segment == temp.getBottomSegment() && !traversed.contains(temp)
					&& (temp.getTile().getPositionOwner(Position.BOTTOM) == null || temp.getTile().getPositionOwner(Position.BOTTOM).isEmpty()
					|| temp.getTile().getPositionOwner(Position.BOTTOM).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.BOTTOM, name);
				traversed.add(temp);
				//container.showFollower(Position.TOP, 3);
				updateConnectedSegments(temp, temp.getBottomSegment(), Position.BOTTOM, name, traversed);
			}
		}
		if(r+1 < GameData.ROWS) {
			TileContainer temp = gameData.getTile(r+1, c);
			if(temp != null && segment == temp.getTopSegment() && !traversed.contains(temp)
					&& (temp.getTile().getPositionOwner(Position.TOP) == null || temp.getTile().getPositionOwner(Position.TOP).isEmpty()
					|| temp.getTile().getPositionOwner(Position.TOP).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.TOP, name);
				traversed.add(temp);
				//container.showFollower(Position.BOTTOM, 3);
				updateConnectedSegments(temp, temp.getTopSegment(), Position.TOP, name, traversed);
			}
		}
		if(c-1 >= 0) {
			TileContainer temp = gameData.getTile(r, c-1);
			if(temp != null && segment == temp.getRightSegment() && !traversed.contains(temp)
					&& (temp.getTile().getPositionOwner(Position.RIGHT) == null || temp.getTile().getPositionOwner(Position.RIGHT).isEmpty()
					|| temp.getTile().getPositionOwner(Position.RIGHT).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.RIGHT, name);
				traversed.add(temp);
				//container.showFollower(Position.LEFT, 3);
				updateConnectedSegments(temp, temp.getRightSegment(), Position.RIGHT, name, traversed);
			}
		}
		if(c+1 < GameData.COLS) {
			TileContainer temp = gameData.getTile(r, c+1);
			if(temp != null && segment == temp.getLeftSegment() && !traversed.contains(temp)
					&& (temp.getTile().getPositionOwner(Position.LEFT) == null || temp.getTile().getPositionOwner(Position.LEFT).isEmpty()
					|| temp.getTile().getPositionOwner(Position.LEFT).equals(name))) {
				temp.getTile().updateSegmentOwners(Position.LEFT, name);
				traversed.add(temp);
				//container.showFollower(Position.RIGHT, 3);
				updateConnectedSegments(temp, temp.getLeftSegment(), Position.LEFT, name, traversed);
			}
		}
	}
	
	/**
	 * Handles the final scoring of the game.
	 */
	public synchronized void handleFinalScoring() {
		
	}
	
	/**
	 * Updates the game board regarding the completed segments.
	 * @param player the player who requested the event
	 */
	public synchronized void handleCompleteSegments(final String player) {
		handleCompleteCloister(player);
		//handleCompleteRoad(player);
	}
	
	/**
	 * Checks for completed cloisters then handles accordingly.
	 * @param player the player who requested the event
	 */
	private void handleCompleteCloister(final String player) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(int r=0; r<GameData.ROWS; ++r) {
					for(int c=0; c<GameData.COLS; ++c) {
						TileContainer container = gameData.getTile(r, c);
						if(container.getTile().getSegment(Position.CENTER) == Segment.CLOISTER && container.getFollowerPosition() != null) {
							int count = 0;
							if(r-1 >= 0 && !gameData.getTile(r-1, c).isHoverTile() && !gameData.getTile(r-1, c).isEmpty())
								count++;
							if(r+1 < GameData.ROWS && !gameData.getTile(r+1, c).isHoverTile() && !gameData.getTile(r+1, c).isEmpty())
								count++;
							if(c-1 >= 0 && !gameData.getTile(r, c-1).isHoverTile() && !gameData.getTile(r, c-1).isEmpty())
								count++;
							if(c+1 < GameData.COLS && !gameData.getTile(r, c+1).isHoverTile() && !gameData.getTile(r, c+1).isEmpty())
								count++;
							if(count == 4) {
								container.getChildren().remove(container.follower);
								if(client.getGame().getPlayerName().equalsIgnoreCase(player)) {
									sendScoreUpdateRequest(container.getTile().getPositionOwner(Position.CENTER), ScoreData.CLOISTER_POINTS);
									sendFollowerUpdateRequest(container.getTile().getPositionOwner(Position.CENTER), 1);
								}
								container.getTile().removeSegment(Position.CENTER);
							}
						}
					}
				}
			}
		});
	}
	
	/**
	 * Updates the player's information.
	 * @param name the player name
	 * @param points the score points
	 */
	public synchronized void updatePlayerData(final String name, final int points) {
		Connection connection = client.getGame().getConnection();
		Event event = new Event(EventType.SCORE_UPDATE_REQUEST, client.getGame().getPlayerName());
		event.addProperty("target", name);
		event.addProperty("points", points);
		connection.sendEvent(event);
	}
	
	/**
	 * Handles the start of a game.
	 * @param names the player names
	 * @param statuses the statuses
	 * @param target the target name
	 * @param row the row
	 * @param column the column
	 * @param mode the mode
	 */
	public synchronized void handleStartGame(final String[] names, final Player.Status[] statuses, 
			final String target, final int row, final int column, final Mode mode) {
		updatePlayerPanel(names, statuses, mode);
		updateFollowerPanel();
		addStartingTile(row, column);
		
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				scoreLabel.setText(LocalMessages.getString("ScoreTitle"));
				pointsLabel.setText(CommonUtil.convertIntegerToString(0));
			}
		});
		
		if(isPlayerTurn(target))
			sendTurnRequest();
	}
	
	/**
	 * Updates the player's score
	 * @param name the player name
	 * @param score the score
	 */
	public synchronized void updatePlayerScore(final String name, final int score) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				if(name.equals(client.getGame().getPlayerName()))
					pointsLabel.setText(CommonUtil.convertIntegerToString(score));
				scoreData.setPlayerScore(name, score);
			}
		});
	}
	
	/**
	 * Updates the deck information.
	 * @param tilesLeft the number of tiles left
	 */
	public synchronized void updateDeckInfo(final int tilesLeft) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				double[] fileLimits = {0, 1, 2};
				String[] fileStrings = {
					LocalMessages.getString("NoTiles"),
					LocalMessages.getString("OneTile"),
					LocalMessages.getString("MultipleTiles")
				};
				
				MessageFormat messageForm = new MessageFormat("");
				ChoiceFormat choiceForm = new ChoiceFormat(fileLimits, fileStrings);
				String pattern = LocalMessages.getString("DeckInfo");
				Format[] formats = {choiceForm, null, NumberFormat.getInstance()};
				messageForm.applyPattern(pattern);
				messageForm.setFormats(formats);
				
				Object[] messageArguments = {tilesLeft, "the deck", tilesLeft};
				String result = messageForm.format(messageArguments);
				deckLabel.setText(result);
			}
		});
	}
	
	/**
	 * Handles the start of the turn
	 * @param playerName the player name
	 * @param tile the tile name
	 * @param targetName the target name
	 * @param targetIndex the target index
	 * @param numOfPlayers the number of players
	 * @param tilesLeft the number of tiles in deck
	 */
	public synchronized void handleStartTurn(final String playerName, final String tile, final String targetName,
			final int targetIndex, final int numOfPlayers, final int tilesLeft) {
		if(tile == null) // return if there is no tile
			return;
		
		updateDeckInfo(tilesLeft);
		
		if(client.getGame().getMode() == Mode.SYNC) {
			showCurrentPlayerTurn(targetIndex, numOfPlayers);
			if(client.getGame().getPlayerName().equalsIgnoreCase(targetName))
				startTurn(tile);
		} else if(client.getGame().getMode() == Mode.ASYNC) {
			if(client.getGame().getPlayerName().equalsIgnoreCase(playerName))
				startTurn(tile);
			else
				firePreviewTileEvent(true);
		}
	}
	
	/**
	 * Handles the end of a turn.
	 * @param success true if deck is not empty
	 * @param playerName the player name
	 * @param targetName the target name
	 * @param title the title
	 * @param message the message
	 * @param isPlayerTurn flag to see if it is player's turn
	 */
	public synchronized void handleEndTurn(final boolean success, final String playerName, final String targetName,
			final String title, final String message, final boolean isPlayerTurn) {
		if(client.getGame().getPlayerName().equalsIgnoreCase(playerName))
			removePreviewTiles();
		if(!success) {
			sendEndGameRequest(title, message);
			return;
		}
		if(client.getGame().getMode() == Mode.SYNC) {
			if(success && client.getGame().getPlayerName().equalsIgnoreCase(targetName)) {
				sendTurnRequest();
			}
		} else if(client.getGame().getMode() == Mode.ASYNC) {
			if(success && client.getGame().getPlayerName().equalsIgnoreCase(playerName)) {
				sendTurnRequest();
			}
		}
	}
	
	/**
	 * Handles the quit event.
	 */
	public synchronized void handleQuitEvent(final boolean gameInProgress, final boolean finished, final String title,
			final String message, final String playerName, final String[] names, final Player.Status[] statuses,
			final int numOfPlayers, final Mode mode) {
		if(gameInProgress) {
			updatePlayerPanel(names, statuses, mode);
			if(finished && message != null) {
				if(playerName.equals(client.getGame().getPlayerName())) {
					client.getStage().getOnCloseRequest().handle(null);
				} else {
					sendEndGameRequest(title, message);
				}
			} else if(client.getGame().getMode() == Mode.SYNC && client.getGame().getPlayerName().equalsIgnoreCase(playerName)) {
				sendEndTurnRequest();
			}
		}
		else {
			getLobbyController().updatePlayerIcons(numOfPlayers);
			getLobbyController().handleStartAvailability(numOfPlayers);
		}
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
				rootPane.setDisable(state);
			}
		});
	}
	
	/**
	 * Sets the grid pane's new height.
	 */
	public synchronized void updateGridHeight() {
		gridPane.setPrefHeight(GameData.TILE_SIZE * GameData.ROWS + (GameData.GAP_SIZE * (GameData.ROWS + 1)) + GameData.OFFSET);
		scrollPane.setPrefHeight(gridPane.getPrefHeight());
	}
	
	/**
	 * Sets the grid pane's new width.
	 */
	public synchronized void updateGridWidth() {
		gridPane.setPrefWidth(GameData.TILE_SIZE * GameData.COLS + (GameData.GAP_SIZE * (GameData.COLS + 1)) + GameData.OFFSET);
		scrollPane.setPrefWidth(gridPane.getPrefWidth());
	}
	
	/**
	 * Returns true if the specified player name is the client's player name.
	 * @param player the player name
	 * @return a boolean
	 */
	public synchronized boolean isPlayerTurn(final String player) {
		return client.getGame().getMode() == Mode.ASYNC || player.equalsIgnoreCase(client.getGame().getPlayerName());
	}

	/**
	 * Returns the score data.
	 * @return the score data
	 */
	public synchronized ScoreData getScoreData() {
		return scoreData;
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
