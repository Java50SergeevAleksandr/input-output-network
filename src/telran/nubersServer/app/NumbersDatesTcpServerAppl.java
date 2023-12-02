package telran.nubersServer.app;

import telran.net.TcpServer;

public class NumbersDatesTcpServerAppl {
	private static final int PORT = 5000;

	public static void main(String[] args) throws Exception {
		NumbersDatesProtocol numberDatesProtocol = new NumbersDatesProtocol();
		TcpServer server = new TcpServer(PORT, numberDatesProtocol);
		server.run();
	}
}
