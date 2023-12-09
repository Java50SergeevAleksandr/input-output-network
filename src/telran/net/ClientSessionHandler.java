package telran.net;

import java.net.*;
import java.io.*;

public class ClientSessionHandler implements Runnable {
	final Socket socket;
	ApplProtocol protocol;

	public ClientSessionHandler(Socket socket, ApplProtocol protocol) throws Exception {
		this.socket = socket;
		this.protocol = protocol;

	}

	@Override
	public void run() {
		try (socket;
				ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());) {
			while (true) {
				Request request = (Request) reader.readObject();
				Response response = protocol.getResponse(request);
				writer.writeObject(response);
				System.out.println("Server sent a response --" + response.toString() + "-- to client"
						+ socket.getRemoteSocketAddress());
				writer.reset();
			}

		} catch (EOFException e) {
			System.out.println("Client closed connection");
		} catch (Exception e) {
			System.out.println("Abnormal closing connection");
		}

	}

}
