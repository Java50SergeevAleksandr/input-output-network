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
		boolean isOpen = true;
		int totalIdleTime = 0;
		while (!tcpServer.executor.isShutdown() && isOpen) {
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
				totalIdleTime += TcpServer.IDLE_TIMEOUT;
				if (totalIdleTime >= TcpServer.TOTAL_IDLE_TIMEOUT
						&& tcpServer.connectedClients.get() > tcpServer.nThreads) {
					isOpen = false;
					tcpServer.connectedClients.decrementAndGet();
				}
			} catch (EOFException e) {
				System.out.println("Client " + socket.getRemoteSocketAddress() + " closed connection");
			} catch (Exception e) {
				System.out.println("Abnormal closing connection, client " + socket.getRemoteSocketAddress());
			}
		}

	}

}
