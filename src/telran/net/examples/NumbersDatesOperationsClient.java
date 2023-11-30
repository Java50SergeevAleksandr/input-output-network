package telran.net.examples;

import java.io.*;
import java.net.*;
import telran.view.*;

public class NumbersDatesOperationsClient {
	private static BufferedReader reader;
	private static PrintStream writer;
	private static final int PORT = 6000;
	private static final String HOST = "localhost";

	public static void main(String[] args) throws Exception {
		InputOutput io = new SystemInputOutput();
		Socket socket = new Socket(HOST, PORT);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintStream(socket.getOutputStream());
		Menu menu = new Menu("Number Operations Client", Item.of("Add", ioM -> runProtocol("Add", ioM)),
				Item.of("Subtract", ioM -> runProtocol("Subtract", ioM)),
				Item.of("Divide", ioM -> runProtocol("Divide", ioM)),
				Item.of("Multiply", ioM -> runProtocol("Multiply", ioM)), Item.of("Exit", ioM -> {
					try {
						socket.close();
					} catch (IOException e) {
					}
				}, true));
		menu.perform(io);
	}

	private static void runProtocol(String type, InputOutput io) {
		double[] operands = getOperands(io);
		writer.printf("%s#%s#%s%n", type, operands[0] + "", operands[1] + "");
		try {
			io.writeLine(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static double[] getOperands(InputOutput io) {
		return new double[] { io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number") };
	}

}
