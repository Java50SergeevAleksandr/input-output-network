package telran.nubersServer.app;

import java.io.IOException;

import telran.net.TcpClientHandler;
import telran.view.*;

public class NumbersDatesTcpClientAppl {
	private static final String HOST = "localhost";
	private static final int PORT = 5000;

	public static void main(String[] args) {
		InputOutput io = new SystemInputOutput();
		try (TcpClientHandler handler = new TcpClientHandler(HOST, PORT);) {
			Menu menu = new Menu("Number Operations Client", getItems(handler));
					
			menu.perform(io);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Item[] getItems(TcpClientHandler handler) {
		Item items[] = { NumbersOperationsMenu.getNumberOperationsItem("Number Operations", handler),
				DatesOperationsMenu.getDateOperationsItem("Date Operations", handler),
				Item.of("Create Error - Wrong Type", ioM -> runProtocol("wrongType", ioM, handler)),
				Item.of("Create Error - Wrong Data",
						ioM -> runErrorProtocol(NumbersDatesProtocol.getMultiplyProtocol(), ioM, handler)),
				Item.of("Exit", io -> {
					try {
						handler.close();
					} catch (IOException e) {
						throw new RuntimeException(e.getMessage());
					}
				}, true) };
		return items;
	}

	private static void runProtocol(String string, InputOutput io, TcpClientHandler handler) {
		double response = handler.send(string, new double[] {});
		io.writeObjectLine(response);
	}

	private static void runErrorProtocol(String string, InputOutput io, TcpClientHandler handler) {
		double response = handler.send(string, "");
		io.writeObjectLine(response);
	}

}
