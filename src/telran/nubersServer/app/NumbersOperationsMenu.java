package telran.nubersServer.app;

import telran.net.NetworkHandler;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class NumbersOperationsMenu {
	static NetworkHandler handler;
	static String name;

	public static Item getNumberOperationsItem(String name, NetworkHandler handler) {
		NumbersOperationsMenu.handler = handler;
		NumbersOperationsMenu.name = name;

		return Item.of(name, NumbersOperationsMenu::performMethod);

	}

	private static void runProtocol(InputOutput io, String string) {
		io.writeObjectLine(handler.send(string, getOperands(io)));
	}

	private static double[] getOperands(InputOutput io) {
		return new double[] { io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number") };
	}

	static void performMethod(InputOutput io1) {
		Item[] items = { Item.of("Add two numbers", io -> runProtocol(io, NumbersDatesProtocol.getAddProtocol())),
				Item.of("Subtract two numbers", io -> runProtocol(io, NumbersDatesProtocol.getSubtractProtocol())),
				Item.of("Divide two numbers", io -> runProtocol(io, NumbersDatesProtocol.getDivideProtocol())),
				Item.of("Multiply two numbers", io -> runProtocol(io, NumbersDatesProtocol.getMultiplyProtocol())),
				Item.exit()

		};
		Menu menu = new Menu(name, items);
		menu.perform(io1);
	}
}
