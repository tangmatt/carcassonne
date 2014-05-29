package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DefaultAcceptor extends Acceptor {
	private final ServerSocket listener;
	
	public DefaultAcceptor(final Service service, final ServerSocket listener) {
		super(service);
		this.listener = listener;
	}

	@Override
	public Connection accept() throws IOException {
		Socket socket = listener.accept();
		return new DefaultConnection(service, socket);
	}
}
