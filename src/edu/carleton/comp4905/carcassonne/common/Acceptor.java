package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;

public abstract class Acceptor {
	protected Service service;
	
	public Acceptor(Service service) {
		this.service = service;
	}
	
	public abstract Connection accept() throws IOException;
}
