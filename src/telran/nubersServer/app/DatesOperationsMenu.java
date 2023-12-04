package telran.nubersServer.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import telran.net.NetworkHandler;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class DatesOperationsMenu {
	static NetworkHandler handler;

	public static Item getDateOperationsItem(String name, NetworkHandler handler) {
		DatesOperationsMenu.handler = handler;
		return Item.of(name, io -> {
			Item items[] = { Item.of("Date after a given number of days", DatesOperationsMenu::dateAdding),
					Item.of("Date before a given number of days", DatesOperationsMenu::dateSubtracting),
					Item.of("Days between two dates", DatesOperationsMenu::daysBetween), Item.exit() };
			Menu menu = new Menu(name, new ArrayList<Item>(Arrays.asList(items)));
			menu.perform(io);
		});
	}

	static private void dateAfter(InputOutput io, boolean isSubtract) {

		LocalDate date = io.readIsoDate("Enter date in ISO format ", "Wrong Date ");
		int days = io.readInt("Enter number of days", "Wrong number of days", 1, Integer.MAX_VALUE);
		if (isSubtract) {
			days = -days;
		}
		DateDays dateDays = new DateDays(date, days);
		io.writeObjectLine(handler.send(NumbersDatesProtocol.getDays(), dateDays));

	}

	static void dateAdding(InputOutput io) {
		dateAfter(io, false);
	}

	static void dateSubtracting(InputOutput io) {
		dateAfter(io, true);
	}

	static void daysBetween(InputOutput io) {
		LocalDate date1 = io.readIsoDate("Enter first Date", "Wrong Date ");
		LocalDate date2 = io.readIsoDate("Enter second Date", "Wrong Date  ");

		io.writeObjectLine(handler.send(NumbersDatesProtocol.getDaysBetween(), new LocalDate[] { date1, date2 }));

	}
}
