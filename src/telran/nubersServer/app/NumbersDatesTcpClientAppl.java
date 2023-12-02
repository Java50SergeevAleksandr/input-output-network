package telran.nubersServer.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import telran.net.TcpClientHandler;
import telran.view.*;

public class NumbersDatesTcpClientAppl {
	private static final String HOST = "localhost";
	private static final int PORT = 5000;

	public static void main(String[] args) {
		InputOutput io = new SystemInputOutput();
		try (TcpClientHandler handler = new TcpClientHandler(HOST, PORT);) {
			Menu menu = new Menu("Number Operations Client",
					Item.of("Add", ioM -> runProtocol(NumbersDatesProtocol.getAddProtocol(), ioM, handler)),
					Item.of("Subtract", ioM -> runProtocol(NumbersDatesProtocol.getSubtractProtocol(), ioM, handler)),
					Item.of("Divide", ioM -> runProtocol(NumbersDatesProtocol.getDivideProtocol(), ioM, handler)),
					Item.of("Multiply", ioM -> runProtocol(NumbersDatesProtocol.getMultiplyProtocol(), ioM, handler)),
					Item.of("Create Error - Wrong Type", ioM -> runProtocol("wrongType", ioM, handler)),
					Item.of("Create Error - Wrong Data",
							ioM -> runErrorProtocol(NumbersDatesProtocol.getMultiplyProtocol(), ioM, handler)),
					Item.of("Exit", ioM -> {
						try {
							handler.close();
						} catch (IOException e) {
						}
					}, true));
			menu.perform(io);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void runProtocol(String string, InputOutput io, TcpClientHandler handler) {
		Double response = handler.send(string, new ArrayList<>(Arrays.asList(getOperands(io))));
		io.writeObjectLine(response);
	}

	private static void runErrorProtocol(String string, InputOutput io, TcpClientHandler handler) {
		Double response = handler.send(string, "");
		io.writeObjectLine(response);
	}

	private static Double[] getOperands(InputOutput io) {
		return new Double[] { io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number") };
	}
}
