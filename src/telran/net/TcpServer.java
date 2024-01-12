package telran.net;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer implements Runnable, AutoCloseable {
	public static final int IDLE_TIMEOUT = 100;
	private int port;
	private ApplProtocol protocol;
	private ServerSocket serverSocket;
	ExecutorService executor;
	private int nThreads = Runtime.getRuntime().availableProcessors();

	public TcpServer(int port, ApplProtocol protocol) throws Exception {
		this.port = port;
		this.protocol = protocol;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(IDLE_TIMEOUT);
		executor = Executors.newFixedThreadPool(nThreads);
	}

	@Override
	public void run() {
		System.out.println("Server is listening on port " + port);
		while (!executor.isShutdown()) {
			try {
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(IDLE_TIMEOUT);
				ClientSessionHandler client = new ClientSessionHandler(socket, protocol, this);
				System.out.println("Client " + socket.getRemoteSocketAddress() + " is connected");
				executor.execute(client);
			} catch (SocketTimeoutException e) {
				// for exit from accept to another iteration of cycle
			} catch (Exception e) {
				throw new RuntimeException(e.toString());
			}

		}
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}

	public void shutdown() {
		executor.shutdownNow();
	}
}
