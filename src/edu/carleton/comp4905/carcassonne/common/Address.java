package edu.carleton.comp4905.carcassonne.common;

public class Address {
	private String host;
	private int port;
	
	public Address(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getHostname() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean equals(Address addr) {
		return host.equals(addr.getHostname()) && port == addr.getPort();
	}
}
