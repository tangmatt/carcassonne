package edu.carleton.comp4905.carcassonne.client;

import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import edu.carleton.comp4905.carcassonne.client.handlers.PlayerViewHandler;
import edu.carleton.comp4905.carcassonne.common.PlatformManager;
import edu.carleton.comp4905.carcassonne.common.ResourceManager;
import edu.carleton.comp4905.carcassonne.common.StringConstants;
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
	private TileManager tileManager;
	private ClientData model;
	private MouseEvent mouseEvent;
	private LobbyDialog lobbyDialog;
	private GameClient client;
	private Random random;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null);
		tileManager = TileManager.getInstance();
		model = new ClientData();
		random = new Random();
		
		ImageView[] playerViews = new ImageView[] { player1, player2, player3, player4, player5 };
		ImageView[] followerViews = new ImageView[] { meeple1, meeple2, meeple3, meeple4, meeple5, meeple6, meeple7 };
		model.addPlayerViews(playerViews, createPopOver());
		model.addFollowerViews(followerViews);
		setPreviewTiles(tileManager.getEmptyTile()); // initializes the preview tiles to be empty
		
		createGameBoard();
		createLobby();
	}
	
	/**
	 * Adds a TileContainer object to specified row and column of layout.
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container a TileContainer
	 */
	public void addTile(final int r, final int c, final TileContainer container) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				model.setTile(r, c, container);
				gridPane.add(container, c, r);
			}
		});
	}
	
	/**
	 * Places tiles onto the tile preview displays.
	 * @param tile a GameTile
	 */
	public void setPreviewTiles(final GameTile tile) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				setRotatedPreviews(tile);
				if(previewPane.getChildren().isEmpty())
					previewPane.getChildren().addAll(model.getPreviews());
					firePreviewTileEvent(false);
			}
		});
	}
	
	/**
	 * Determines whether the chosen tile can be placed adjacent to existing tiles.
	 * @param preview a TilePreview
	 */
	public void handleHints(final TilePreview preview) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				refresh();
				int numOfHints = 0;
				for(int c=0; c<ClientData.COLS; ++c) {
					for(int r=0; r<ClientData.ROWS; ++r) {
						TileContainer currTile;
						currTile = model.getTile(r, c);
						if(currTile != null) {
							// compare existing tile with new tile and indicating if matching
							numOfHints += showHint(Side.TOP, r, c, preview, currTile.getTopSegment());
							numOfHints += showHint(Side.RIGHT, r, c, preview, currTile.getRightSegment());
							numOfHints += showHint(Side.BOTTOM, r, c, preview, currTile.getBottomSegment());
							numOfHints += showHint(Side.LEFT, r, c, preview, currTile.getLeftSegment());
						}
					}
				}
				// if there are no place-able cards, discard current tile and re-draw
				if(numOfHints == 0)
					startTurn();
			}
		});
	}
	
	/**
	 * Manages the empty tile adjacent to an existing tile if there is a match.
	 * @param side a Side
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container an AbstractTile
	 * @param segment a Segment
	 * @return Integer
	 */
	private int showHint(final Side side, final int r, final int c, final AbstractTile container, final Segment segment) {
		try {
			TileContainer selected = null;
			if(side == Side.TOP && model.getTile(r-1, c).isEmpty() && container.matchesBottomSegment(segment)) {
				selected = model.getTile(r-1, c);
				selected.setSelected(true);
				selected.addMouseListener(this,
						new TileContainerHandler(this, r-1, c),
						new TileHoverHandler(this),
						new TileExitHandler());
			}
			else if(side == Side.RIGHT && model.getTile(r, c+1).isEmpty() && container.matchesLeftSegment(segment)) {
				selected = model.getTile(r, c+1);
				selected.setSelected(true);
				selected.addMouseListener(this, 
						new TileContainerHandler(this, r, c+1),
						new TileHoverHandler(this),
						new TileExitHandler());
			}
			else if(side == Side.BOTTOM && model.getTile(r+1, c).isEmpty() && container.matchesTopSegment(segment)) {
				selected = model.getTile(r+1, c);
				selected.setSelected(true);
				selected.addMouseListener(this, 
						new TileContainerHandler(this, r+1, c),
						new TileHoverHandler(this),
						new TileExitHandler());
			}
			else if(side == Side.LEFT && model.getTile(r, c-1).isEmpty() && container.matchesRightSegment(segment)) {
				selected = model.getTile(r, c-1);
				selected.setSelected(true);
				selected.addMouseListener(this,
						new TileContainerHandler(this, r, c-1),
						new TileHoverHandler(this),
						new TileExitHandler());
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return 0;
		}
		
		return 1;
	}
	
	/**
	 * Checks to see if the tile to be placed matches the surrounding tiles.
	 * @param side a Side
	 * @param r a row (integer)
	 * @param c a column (integer)
	 * @param container an AbstractTile
	 * @param segment a Segment
	 * @return Integer
	 */
	private boolean matchesSurroundingTiles(final Side side, final int r, final int c, final AbstractTile container, final Segment segment) {
		try {
			if(side == Side.TOP && (model.getTile(r-1, c).isEmpty() || container.matchesBottomSegment(segment)))
				return true;
			else if(side == Side.RIGHT && (model.getTile(r, c+1).isEmpty() || container.matchesLeftSegment(segment)))
				return true;
			else if(side == Side.BOTTOM && (model.getTile(r+1, c).isEmpty() || container.matchesTopSegment(segment)))
				return true;
			else if(side == Side.LEFT && (model.getTile(r, c-1).isEmpty() || container.matchesRightSegment(segment)))
				return true;
		} catch(ArrayIndexOutOfBoundsException e) {
			// do nothing
		}
		
		return false;
	}
	
	/**
	 * Initializes the start of a game.
	 */
	public void startGame() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				startTurn(); // TODO temporary; for testing
			}
		});
	}
	
	/**
	 * Handles the start of the turn for the current client.
	 */
	public void startTurn() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				int index = random.nextInt(TileManager.NUM_OF_TILES) + 1;
				setPreviewTiles(tileManager.getTile("tile" + index + ".png"));
			}
		});
	}
	
	/**
	 * Handles the end of the turn for current client.
	 */
	public void endTurn() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : model.getPreviews())
					preview.setSelected(false);
				for(int c=0; c<ClientData.COLS; ++c) {
					for(int r=0; r<ClientData.ROWS; ++r) {
						model.getTile(r, c).setSelected(false);
						model.getTile(r, c).removeMouseListener();
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
				TilePreview[] previews = model.getPreviews();
				for(int i=0, degrees=0; i<previews.length; ++i, degrees+=90) {
					boolean original = (degrees == 0);
					if(previews[i] == null)
						previews[i] = new TilePreview(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					else
						previews[i].addTile(tileManager.getTile(original ? tile.getName() : tile.getName()+degrees));
					if(!previews[i].getTile().getName().equals(StringConstants.EMPTY_TILE))
						previews[i].addMouseListener(GameController.this, new TilePreviewHandler(GameController.this));
				}
			}
		});
	}
	
	/**
	 * Refreshes the preview and game tiles.
	 */
	public void refresh() {
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
	public void refreshPreviews() {
		for(TilePreview preview : model.getPreviews())
			preview.setSelected(false);
	}
	
	/**
	 * Refreshes the game tiles.
	 */
	public void refreshGameTiles() {
		for(int c=0; c<ClientData.COLS; ++c) {
			for(int r=0; r<ClientData.ROWS; ++r) {
				model.getTile(r, c).setEffect(null);
				model.getTile(r, c).setSelected(false);
				model.getTile(r, c).removeMouseListener();
			}
		}
	}
	
	/**
	 * Fires the event to handle mouse presses on preview tiles.
	 * @param keepState a boolean
	 */
	public void firePreviewTileEvent(boolean keepState) {
		if(keepState)
			Event.fireEvent(model.getSelectedPreviewTile(), mouseEvent);
		else
			Event.fireEvent(model.getPreviews()[0], mouseEvent);
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
	public void showLobbyDialog() {
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
				for(int c=0; c<ClientData.COLS; ++c) {
					for(int r=0; r<ClientData.ROWS; ++r) {
						TileContainer container;
						if(r == ClientData.CENTER_ROW && c == ClientData.CENTER_COL)
							container = new TileContainer(tileManager.getStarterTile());
						else
							container = new TileContainer(tileManager.getEmptyTile());
						addTile(r, c, container);
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
	public void updatePlayerPanel(final String[] names, final boolean[] statuses) {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				Map<ImageView, PopOver> players = model.getPlayerViews();
				int i=0;
				for(ImageView imageView : players.keySet()) {
					if(i >= statuses.length) // only loop through the number of connected players
						break;
					
					PopOver popOver = players.get(imageView);
					
					// create node to place into PopOver content
					Label label = new Label(names[i]);
					label.setPadding(new Insets(5, 5, 5, 5));
					
					// update player icons according to connection status
					Image image = ResourceManager.getImageFromResources(statuses[i] ? "joined.png" : "disconnected.png");
					imageView.setImage(image);
					imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, new PlayerViewHandler(imageView, popOver, label));
					i++;
				}
			}
		});
	}
	
	/**
	 * Shows the amount of followers for the player.
	 */
	public void updateFollowerPanel() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				ImageView[] followers = model.getFollowerViews();
				for(int i=followers.length-1; i>=0; --i) {
					if(i < (followers.length - model.getNumOfFollowers()))
						followers[i].setOpacity(0.2f);
					Image image = ResourceManager.getImageFromResources("meeple" + model.getIndex() + ".png");
					followers[i].setImage(image);
				}
			}
		});
	}
	
	/**
	 * Clear the preview tiles and replace with empty tiles.
	 */
	public void clearPreviews() {
		PlatformManager.run(new Runnable() {
			@Override
			public void run() {
				for(TilePreview preview : model.getPreviews()) {
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
	public void blurGame(final boolean state) {
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
	public HBox getPreviewPane() {
		return previewPane;
	}
	
	/**
	 * Returns the pane containing the game tiles.
	 * @return a GridPane
	 */
	public GridPane getGridPane() {
		return gridPane;
	}
	
	/**
	 * Returns the Model object.
	 * @return a Model
	 */
	public ClientData getModel() {
		return model;
	}
	
	/**
	 * Returns the LobbyController object.
	 * @return a LobbyController
	 */
	public LobbyController getLobbyController() {
		return lobbyDialog.getController();
	}
	
	/**
	 * Returns the GameClient object.
	 * @return a GameClient;
	 */
	public GameClient getGameClient() {
		return client;
	}
	
	/**
	 * Initializes data for this controller.
	 * @param client a GameClient
	 */
	public void initData(final GameClient client) {
		this.client = client;
	}
}
