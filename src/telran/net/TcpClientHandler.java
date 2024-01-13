package telran.net;

import java.io.*;
import java.net.*;

public class TcpClientHandler implements Closeable, NetworkHandler {
	Socket socket;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	String host;
	int port;

	public TcpClientHandler(String host, int port) throws Exception {
		this.host = host;
		this.port = port;
		connect(host, port);
	}

	private void connect(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		reader.close();
		writer.close();
		socket.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable requestData) {
		Request request = new Request(requestType, requestData);
		while (true) {
			try {
				writer.writeObject(request);
				Response response = (Response) reader.readObject();
				if (response.code() != ResponseCode.OK) {
					throw new Exception(response.responseData().toString());
				}
				return (T) response.responseData();

			} catch (SocketException e) {
				try {
					System.out.println("Reconnect");
					close();
					connect(host, port);

				} catch (IOException e1) {
					throw new RuntimeException(e1.getMessage());
				}

			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}