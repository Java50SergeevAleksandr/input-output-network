package telran.view.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;
import telran.view.*;

class InputOutputTest {
	InputOutput io = new SystemInputOutput();

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testReadEmployeeString() {
		Employee empl = io.readObject("Enter employee <id>#<name>#<iso birthdate>#<department>#<salary>",
				"Wrong Employee", str -> {
					String[] tokens = str.split("#");
					if (tokens.length != 5) {
						throw new RuntimeException("must be 5 tokens");
					}
					long id = Long.parseLong(tokens[0]);
					String name = tokens[1];
					String department = tokens[3];
					int salary = Integer.parseInt(tokens[4]);
					LocalDate birthDate = LocalDate.parse(tokens[2]);
					return new Employee(id, name, department, salary, birthDate);
				});
		io.writeObjectLine(empl);
	}

	@Test
	void testReadEmployeeBySeparateField() {
		// TODO
		// id in range [100000-999999]
		// name - more than two letters where first one is a capital
		// birthDate - any localdate in range [1950-12-31 - 2003-12-31
		// department - one out of ["QA", "Development", "Audit", "Accounting",
		// "Management"
		// salary - integer number in range [7000 - 50000]
	}

	@Test
	void testSimpleArithmeticCalculator() {
		// TODO
	}

}