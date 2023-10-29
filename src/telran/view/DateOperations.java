package telran.view;

import static telran.view.Item.exit;
import static telran.view.Item.of;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateOperations extends Menu {
	public DateOperations(String name) {
		super(name, getMenuItems());
	}

	private static Item[] getMenuItems() {
		return new Item[] { of("Date after given numbers of days", DateOperations::after),
				of("Date before given numbers of days", DateOperations::before),
				of("Days between two dates", DateOperations::between), exit() };
	}

	private static void after(InputOutput io) {
		Object operands[] = getOperands(io);
		LocalDate date = (LocalDate) operands[0];
		io.writeObjectLine(date.plusDays((long) operands[1]));
	}

	private static Object[] getOperands(InputOutput io) {
		return new Object[] { io.readIsoDate("Enter date in ISO (YYYY-MM-DD) format", "Wrong date"),
				io.readLong("Enter numbers of days", "Wrong number") };
	}

	private static LocalDate[] getDates(InputOutput io) {
		return new LocalDate[] { io.readIsoDate("Enter date in ISO (YYYY-MM-DD) format", "Wrong date"),
				io.readIsoDate("Enter second date in ISO (YYYY-MM-DD) format", "Wrong date") };
	}

	private static void before(InputOutput io) {
		Object operands[] = getOperands(io);
		LocalDate date = (LocalDate) operands[0];
		io.writeObjectLine(date.minusDays((long) operands[1]));
	}

	private static void between(InputOutput io) {
		LocalDate[] operands = getDates(io);
		io.writeObjectLine(operands[0].until(operands[1], ChronoUnit.DAYS));
	}
}
