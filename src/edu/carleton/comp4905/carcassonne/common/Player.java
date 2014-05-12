package edu.carleton.comp4905.carcassonne.common;

public class Player {
	private String name;
	private String address;
	private String port;
	
	public Player(PlayerBuilder builder) {
		this.name = builder.name;
		this.address = builder.address;
		this.port = builder.port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPort() {
		return port;
	}
	
	public boolean equals(Player p) {
		return name.equals(p.getName())
				&& address.equals(p.getAddress())
				&& port.equals(p.getPort());
	}
	
	public static class PlayerBuilder implements Builder<Player> {
		private final String name;
		private final String address;
		private final String port;
		
		public PlayerBuilder(String name, String address, String port) {
			this.name = name;
			this.address = address;
			this.port = port;
		}
		
		@Override
		public Player build() {
			return new Player(this);
		}
	}
}
