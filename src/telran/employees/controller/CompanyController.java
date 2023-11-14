package telran.employees.controller;

import java.time.LocalDate;
import java.util.*;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.view.InputOutput;
import telran.view.Item;

public class CompanyController {
	private static final long MIN_ID = 1;
	private static final long MAX_ID = 999999;
	private static final String[] DEPARTMENTS = { "QA", "Development", "Audit", "Accounting", "Management" };
	private static final HashSet<String> SET_DEPARTMENTS = new HashSet<String>(List.of(DEPARTMENTS));
	private static final int MIN_SALARY = 7000;
	private static final int MAX_SALARY = 50000;
	private static final int MIN_YEAR = 1950;
	private static final int MAX_YEAR = 2001;
	private static Company company;

	public static ArrayList<Item> getItems(Company company) {
		CompanyController.company = company;
		List<Item> itemsList = getItemsList();
		ArrayList<Item> res = new ArrayList<>(itemsList);
		return res;
	}

	private static List<Item> getItemsList() {

		return List.of(Item.of("Hire new Employee", CompanyController::addEmployee),
				Item.of("Fire  Employee", CompanyController::removeEmployee),
				Item.of("Display data of Employee", CompanyController::getEmployee),
				Item.of("Display data of all Employees", CompanyController::getEmployees),
				Item.of("Distribution of salary by departments", CompanyController::getDepartmentSalaryDistribution),
				Item.of("Salary distribution per interval", CompanyController::getSalaryDistribution),
				Item.of("Display data of Employees in department", CompanyController::getEmployeesByDepartment),
				Item.of("Display data of Employees by salary", CompanyController::getEmployeesBySalary),
				Item.of("Display data of Employees by age", CompanyController::getEmployeesByAge),
				Item.of("Update salary", CompanyController::updateSalary),
				Item.of("Update department", CompanyController::updateDepartment));
	}

	static void addEmployee(InputOutput io) {
		long id = io.readLong("Enter employee identity", "Wrong identity", MIN_ID, MAX_ID);
		String name = io.readString("Enter name", "Wrong name", str -> str.matches("[A-Z][a-z]{2,}"));
		String department = io.readString("Enter department " + Arrays.deepToString(DEPARTMENTS), "Wrong department",
				SET_DEPARTMENTS);
		int salary = io.readInt("Enter Salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		LocalDate birthDate = io.readIsoDate("Enter birtdate in ISO format", "Wrong birthdate",
				LocalDate.of(MIN_YEAR, 1, 1), LocalDate.of(MAX_YEAR, 12, 31));
		Employee empl = new Employee(id, name, department, salary, birthDate);
		boolean res = company.addEmployee(empl);
		io.writeLine(res ? "Employee has been added" : "Employee already exists");
	}

	static void removeEmployee(InputOutput io) {
		long id = io.readLong("Enter employee ID to remove", "Wrong identity", MIN_ID, MAX_ID);
		Employee res = company.removeEmployee(id);
		io.writeLine(res == null ? "Employee does not exist" : "Employee has been removed");
	}

	static void getEmployee(InputOutput io) {
		long id = io.readLong("Enter employee ID", "Wrong identity", MIN_ID, MAX_ID);
		Employee res = company.getEmployee(id);
		io.writeObjectLine(res == null ? "Employee does not exist" : res);
	}

	static void getEmployees(InputOutput io) {
		List<Employee> employees = company.getEmployees();
		if (employees.isEmpty()) {
			io.writeLine("No active Employees");
		} else {
			displayResult(io, employees);
		}

	}

	static void getDepartmentSalaryDistribution(InputOutput io) {
		List<DepartmentSalary> salary = company.getDepartmentSalaryDistribution();
		displayResult(io, salary);
	}

	static void getSalaryDistribution(InputOutput io) {
		int interval = io.readInt("Enter interval", "Wrong value");
		List<SalaryDistribution> salary = company.getSalaryDistribution(interval);
		displayResult(io, salary);
	}

	static void getEmployeesByDepartment(InputOutput io) {
		String dep = io.readString("Enter department" + Arrays.deepToString(DEPARTMENTS), "Wrong department",
				SET_DEPARTMENTS);
		List<Employee> listOfEmpl = company.getEmployeesByDepartment(dep);
		displayResult(io, listOfEmpl);
	}

	static void getEmployeesBySalary(InputOutput io) {
		int min = io.readInt("Enter min salary", "Wrong value", MIN_SALARY, MAX_SALARY);
		int max = io.readInt("Enter max salary", "Wrong value", MIN_SALARY, MAX_SALARY);
		List<Employee> listOfEmpl = company.getEmployeesBySalary(min, max);
		displayResult(io, listOfEmpl);
	}

	static void getEmployeesByAge(InputOutput io) {
		int min = io.readInt("Enter min age in years ", "Wrong value", LocalDate.now().getYear() - MAX_YEAR,
				LocalDate.now().getYear() - MIN_YEAR);
		int max = io.readInt("Enter max age in years ", "Wrong value", LocalDate.now().getYear() - MAX_YEAR,
				LocalDate.now().getYear() - MIN_YEAR);
		List<Employee> listOfEmpl = company.getEmployeesByAge(min, max);
		displayResult(io, listOfEmpl);
	}

	static void updateSalary(InputOutput io) {
		long id = io.readLong("Enter employee ID", "Wrong identity", MIN_ID, MAX_ID);
		int newSalary = io.readInt("Enter new Salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		Employee empl = company.updateSalary(id, newSalary);
		io.writeLine(empl == null ? "Employee does not exist"
				: "Employee salary has been updated: " + company.getEmployee(id));
	}

	static void updateDepartment(InputOutput io) {
		long id = io.readLong("Enter employee ID", "Wrong identity", MIN_ID, MAX_ID);
		String newDep = io.readString("Enter new Department" + Arrays.deepToString(DEPARTMENTS), "Wrong department",
				SET_DEPARTMENTS);
		Employee empl = company.updateDepartment(id, newDep);
		io.writeLine(empl == null ? "Employee does not exist"
				: "Employee salary has been updated: " + company.getEmployee(id));
	}

	private static <T> void displayResult(InputOutput io, List<T> list) {
		list.forEach(s -> io.writeObjectLine(s));
	}
}