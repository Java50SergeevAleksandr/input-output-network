package telran.view.test;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;
import telran.view.*;

class InputOutputTest {
	InputOutput io = new SystemInputOutput();
	HashSet<String> options = new HashSet<>();
	Predicate<String> predicate;

	public static final String WRONG_EXPRESSION = "Wrong arithmetic expresion syntax";
	public static final String VARIABLE_NOT_DEFINED = "Variable is not defined";
	static HashMap<String, BinaryOperator<Double>> mapOperations;
	static {
		mapOperations = new HashMap<>();
		mapOperations.put("+", (a, b) -> a + b);
		mapOperations.put("-", (a, b) -> a - b);
		mapOperations.put("*", (a, b) -> a * b);
		mapOperations.put("/", (a, b) -> a / b);
	}

	@BeforeEach
	void setUp() throws Exception {
		options.addAll(Set.of("QA", "Development", "Audit", "Accounting", "Management"));
		Pattern pattern = Pattern.compile("[A-Z][a-z]+");
		predicate = s -> pattern.matcher(s).matches();
	}

	@Test
	@Disabled
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
	@Disabled
	void testReadEmployeeBySeparateField() {
		Integer id = io.readInt("Enter employee <id>", "Wrong id", 10, 99);
		io.writeLine("employee <id> " + id);

		Integer salary = io.readInt("Enter employee salary", "Wrong value", 7000, 50000);
		io.writeLine("employee salary " + salary);

		LocalDate birthDate = io.readIsoDate("Enter employee birth date", "Wrong date", LocalDate.parse("1950-12-31"),
				LocalDate.parse("2003-12-31"));
		io.writeLine("employee birth date " + birthDate);

		String department = io.readString("Enter employee department", "Wrong department", options);
		io.writeLine("employee department " + department);

		String name = io.readString("Enter employee name", "Wrong name", predicate);
		io.writeLine("employee name " + name);

		io.writeObjectLine(new Employee(id, name, department, salary, birthDate));
		// name - more than two letters where first one is a capital

	}

	@Test
	void testSimpleArithmeticCalculator() {
		Map<String, Double> variableValues = new HashMap<>();
		variableValues.put("a", 10.);
		variableValues.put("b", 1.);
		variableValues.put("c", 2.);
		String input = io.readString("Enter arithmetic expression");

		double res = 0;
		try {
			res = calculation(input, variableValues);
			io.writeLine("result = " + res);
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}

	}

	static public String javaVariable() {

		return "[a-zA-Z$][\\w$]*|_[\\w$]+";
	}

	public static String arithmeticExpression() {
		String operandRE = operand();
		String operatorRE = operator();
		return String.format("%1$s(%2$s%1$s)*", operandRE, operatorRE);
	}

	public static String operator() {
		return "([-+*/])";
	}

	private static String operand() {
		String numberExp = numberExp();
		String variableExp = javaVariable();
		return String.format("(\\s*\\(*\\s*)*((%s|%s))(\\s*\\)*\\s*)*", numberExp, variableExp);
	}

	private static String numberExp() {
		return "(\\d+\\.?\\d*|\\.\\d+)";
	}

	public static boolean isArithmeticExpression(String expression) {
		boolean res = false;
		if (bracketPairsValidation(expression)) {
			res = expression.matches(arithmeticExpression());
		}
		return res;
	}

	private static boolean bracketPairsValidation(String expression) {
		boolean res = true;
		int count = 0;
		char[] chars = expression.toCharArray();
		int index = 0;
		while (index < chars.length && res) {
			if (chars[index] == '(') {
				count++;
			} else if (chars[index] == ')') {
				count--;
				if (count < 0) {
					res = false;
				}
			}
			index++;
		}
		if (res) {
			res = count == 0;
		}
		return res;
	}

	public static double calculation(String expression, Map<String, Double> variableValues) {
		if (!isArithmeticExpression(expression)) {
			throw new IllegalArgumentException(WRONG_EXPRESSION);
		}
		expression = expression.replaceAll("[()\\s]+", ""); // removing brackets and spaces
		String[] operators = expression.split(operand());
		String[] operands = expression.split(operator());
		double res = getValue(operands[0], variableValues);
		for (int i = 1; i < operands.length; i++) {
			double operand = getValue(operands[i], variableValues);
			res = mapOperations.get(operators[i]).apply(res, operand);
		}

		return res;
	}

	private static double getValue(String operand, Map<String, Double> variableValues) {
		double res = operand.matches(numberExp()) ? Double.parseDouble(operand)
				: variableValues.getOrDefault(operand, Double.NaN);
		if (Double.isNaN(res)) {
			throw new IllegalArgumentException(VARIABLE_NOT_DEFINED);
		}
		return res;
	}
}