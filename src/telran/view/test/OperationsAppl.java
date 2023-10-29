package telran.view.test;

import static telran.view.Item.*;

import telran.view.DateOperations;
import telran.view.Item;
import telran.view.Menu;
import telran.view.NumberOperations;
import telran.view.SystemInputOutput;

public class OperationsAppl {
	public static void main(String[] args) {
		Item[] items = getMenuItems();
		Menu menu = new Menu("Operations", items);
		menu.perform(new SystemInputOutput());
	}

	private static Item[] getMenuItems() {
		return new Item[] { new NumberOperations("Number Operations"), new DateOperations("Date Operations"), exit() };
	}

}
