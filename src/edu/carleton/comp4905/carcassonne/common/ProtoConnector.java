package edu.carleton.comp4905.carcassonne.common;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ProtoConnector extends Connector {
	public ProtoConnector(final Service service) {
		super(service);
	}

	@Override
	public Connection connect(final String host, final int port)
			throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		return new ProtoConnection(service, socket);
	}
}
