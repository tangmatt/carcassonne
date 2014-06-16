package edu.carleton.comp4905.carcassonne.common;

public class Player {
	public static enum Status {
		CONNECTED, DISCONNECTED
	}
	
	private final String name;
	private final String address;
	private final String port;
	private int score;
	private Status status;
	
	public Player(final PlayerBuilder builder) {
		name = builder.name;
		address = builder.address;
		port = builder.port;
		status = builder.status;
		score = 0;
	}
	
	/**
	 * Returns the player's name.
	 * @return a String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the player's address.
	 * @return a String
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Returns the player's port.
	 * @return a String
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Returns the name of the status/
	 * @return a String
	 */
	public String getStatus() {
		return status.name();
	}
	
	/**
	 * Sets the status to specified status.
	 * @param status a Status
	 */
	public void setStatus(final Status status) {
		this.status = status;
	}
	
	/**
	 * Adds points to player's score.
	 * @param points the points to be added
	 */
	public void addPoints(final int points) {
		score += points;
	}
	
	/**
	 * Deducts points from player's score.
	 * @param points the points to be deducted
	 */
	public void deductPoints(final int points) {
		score -= points;
	}
	
	/**
	 * Returns true if the player is connected.
	 * @return a boolean
	 */
	public boolean isConnected() {
		return status == Status.CONNECTED;
	}
	
	/**
	 * Returns true if the player name, address and port is the same as the Player object given associatively.
	 * @param p a Player
	 * @return a boolean
	 */
	public boolean equals(final Player p) {
		return name.equals(p.getName())
				&& address.equals(p.getAddress())
				&& port.equals(p.getPort());
	}
	
	public static class PlayerBuilder implements Builder<Player> {
		private final String name;
		private final String address;
		private final String port;
		private final Status status;
		
		public PlayerBuilder(final String name, final String address, final String port, final Status status) {
			this.name = name;
			this.address = address;
			this.port = port;
			this.status = status;
		}
		
		@Override
		public Player build() {
			return new Player(this);
		}
	}
}
