package telran.net;

import java.io.*;
import java.net.*;

public class TcpClientHandler implements Closeable, NetworkHandler {
	Socket socket;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	String host;
	int port;
	private boolean isSocketConnected = false;

	public TcpClientHandler(String host, int port) throws Exception {
		this.host = host;
		this.port = port;

	}

	private void connect() {
		try {
			socket = new Socket(host, port);
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
			isSocketConnected = true;
		} catch (Exception e) {
			isSocketConnected = false;
			throw new RuntimeException("server is unavailable, please repeat request later on");
		}

	}

	@Override
	public void close() throws IOException {
		reader.close();
		writer.close();
		socket.close();
		isSocketConnected = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable requestData) {
		Request request = new Request(requestType, requestData);
		Response response = null;
		boolean running = false;
		try {
			checkConnection();
			do {
				running = false;

				try {
					writer.writeObject(request);
					response = (Response) reader.readObject();
				} catch (SocketException e) {
					close();
					running = true;
					connect();
				}

			} while (running);
			if (response.code() != ResponseCode.OK) {
				throw new Exception(response.responseData().toString());
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return (T) response.responseData();
	}

	private void checkConnection() throws IOException {
		
		if (isSocketConnected && !socket.isConnected()) {
			close();
		}
		if (!isSocketConnected) {
			connect();
		}
	}
}