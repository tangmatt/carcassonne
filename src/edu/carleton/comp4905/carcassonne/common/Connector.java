package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class Connector {
	protected final Service service;
	
	public Connector(final Service service) {
		this.service = service;
	}
	
	public abstract Connection connect(String host, int port)
			throws UnknownHostException, IOException;
}
