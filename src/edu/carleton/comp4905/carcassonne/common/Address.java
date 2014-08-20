package edu.carleton.comp4905.carcassonne.common;

public class Address {
	private final String host;
	private final int port;
	
	public Address(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Returns the host name.
	 * @return a String
	 */
	public String getHostname() {
		return host;
	}
	
	/**
	 * Returns the port.
	 * @return an Integer
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns true if the address and the port is the same as the Address object given associatively.
	 * @param addr a String
	 * @return a boolean
	 */
	public boolean equals(final Address addr) {
		return host.equals(addr.getHostname()) && port == addr.getPort();
	}
	
	@Override
	public String toString() {
		return "Host: " + host + ", Port: " + port;
	}
}
