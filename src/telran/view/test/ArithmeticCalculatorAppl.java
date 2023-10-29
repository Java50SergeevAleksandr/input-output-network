package telran.view.test;

import telran.view.*;

public class ArithmeticCalculatorAppl {

	public static void main(String[] args) {
		Item[] items = getItems();
		Menu menu = new Menu("Calculator", items);
		menu.perform(new SystemInputOutput());

	}

	private static Item[] getItems() {

		return new Item[] { Item.of("Add", ArithmeticCalculatorAppl::addItem),
				Item.of("Subtract", ArithmeticCalculatorAppl::subtractItem),
				Item.of("Multiply", ArithmeticCalculatorAppl::multiplyItem),
				Item.of("Divide", ArithmeticCalculatorAppl::divideItem),
				Item.exit() };
	}

	static void addItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] + operands[1]);
	}

	private static double[] getOperands(InputOutput io) {

		return new double[] { io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number"),

		};
	}

	static void subtractItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] - operands[1]);
	}

	static void multiplyItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] * operands[1]);
	}

	static void divideItem(InputOutput io) {
		double[] operands = getOperands(io);
		io.writeObjectLine(operands[0] / operands[1]);
	}

}