package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProtoAcceptor extends Acceptor {
	private ServerSocket listener;
	
	public ProtoAcceptor(Service service, ServerSocket listener) {
		super(service);
		this.listener = listener;
	}

	@Override
	public Connection accept() throws IOException {
		Socket socket = listener.accept();
		return new ProtoConnection(service, socket);
	}
}
