package telran.net;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpServer implements Runnable, AutoCloseable {
	public static final int IDLE_TIMEOUT = 100;
	public static final int TOTAL_IDLE_TIMEOUT = 30000;
	private int port;
	private ApplProtocol protocol;
	private ServerSocket serverSocket;
	ExecutorService executor;
	int nThreads = Runtime.getRuntime().availableProcessors();
	AtomicInteger connectedClients = new AtomicInteger();

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
				connectedClients.incrementAndGet();
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

	public void shutdown() throws InterruptedException {
		executor.shutdownNow();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	}
}
