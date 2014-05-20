package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;

public abstract class Acceptor {
	protected final Service service;
	
	public Acceptor(final Service service) {
		this.service = service;
	}
	
	public abstract Connection accept() throws IOException;
}
