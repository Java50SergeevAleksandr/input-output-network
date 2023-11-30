package telran.net.examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NumbersDatesOperationsServer {
	private static final int PORT = 6000;

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server is listening on port:" + PORT);
		while (true) {
			Socket streamSocket = serverSocket.accept();
			System.out.println("Client " + streamSocket.getRemoteSocketAddress() + " is connected");
			runProtocol(streamSocket);
		}
	}

	private static void runProtocol(Socket socket) {
		try (socket;
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream writer = new PrintStream(socket.getOutputStream())) {
			boolean running = true;
			while (running) {
				String request = reader.readLine();
				if (request == null) {
					System.out.println("Client " + socket.getRemoteSocketAddress() + " closed connection");
					running = false;
				} else {
					String response = getResponse(request);
					writer.println(response);
				}
			}
		} catch (Exception e) {
			System.out.println("Abnormal socket closing");
		}

	}

	private static String getResponse(String request) {
		String response = "request must be in format <type#arg1#arg2>";
		String[] tokens = request.split("#");
		if (tokens.length == 3) {
			response = switch (tokens[0]) {
			case "Add" -> (Double.parseDouble(tokens[1]) + Double.parseDouble(tokens[2])) + "";
			case "Subtract" -> (Double.parseDouble(tokens[1]) - Double.parseDouble(tokens[2])) + "";
			case "Divide" -> (Double.parseDouble(tokens[1]) / Double.parseDouble(tokens[2])) + "";
			case "Multiply" -> (Double.parseDouble(tokens[1]) * Double.parseDouble(tokens[2])) + "";
			default -> "Wrong type";
			};
		}
		return response;
	}
}
