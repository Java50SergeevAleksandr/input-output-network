package telran.view;

import static telran.view.Item.*;

public class NumberOperations extends Menu {
	public NumberOperations(String name) {
		super(name, getMenuItems());
	}

	private static Item[] getMenuItems() {
		return new Item[] { of("Add two numbers", NumberOperations::add),
				of("Divide two numbers", NumberOperations::divide),
				of("Subtract two numbers", NumberOperations::subtract),
				of("Multiply two numbers", NumberOperations::multiply), exit() };
	}

	private static void add(InputOutput io) {
		double operands[] = getOperands(io);
		io.writeObjectLine(operands[0] + operands[1]);
	}

	private static double[] getOperands(InputOutput io) {
		return new double[] { io.readDouble("Enter first number", "Wrong number"),
				io.readDouble("Enter second number", "Wrong number") };
	}

	private static void subtract(InputOutput io) {
		double operands[] = getOperands(io);
		io.writeObjectLine(operands[0] - operands[1]);
	}

	private static void divide(InputOutput io) {
		double operands[] = getOperands(io);
		io.writeObjectLine(operands[0] / operands[1]);
	}

	private static void multiply(InputOutput io) {
		double operands[] = getOperands(io);
		io.writeObjectLine(operands[0] * operands[1]);
	}
}
