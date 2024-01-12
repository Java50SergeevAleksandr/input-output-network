package telran.net;

import java.net.*;
import java.io.*;

public class ClientSessionHandler implements Runnable {
	final Socket socket;
	ApplProtocol protocol;
	TcpServer tcpServer;

	public ClientSessionHandler(Socket socket, ApplProtocol protocol, TcpServer tcpServer) throws Exception {
		this.socket = socket;
		this.protocol = protocol;
		this.tcpServer = tcpServer;

	}

	@Override
	public void run() {
		while (!tcpServer.executor.isShutdown()) {
			try (socket;
					ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());) {

				Request request = (Request) reader.readObject();
				Response response = protocol.getResponse(request);
				writer.writeObject(response);
				System.out.println(
						"Server sent a response --" + response + "-- to client" + socket.getRemoteSocketAddress());
				writer.reset();

			} catch (SocketTimeoutException e) {
				// for exit from readObject to another iteration of cycle
			} catch (EOFException e) {
				System.out.println("Client " + socket.getRemoteSocketAddress() + " closed connection");
			} catch (Exception e) {
				System.out.println("Abnormal closing connection, client " + socket.getRemoteSocketAddress());
			}
		}

	}

}
