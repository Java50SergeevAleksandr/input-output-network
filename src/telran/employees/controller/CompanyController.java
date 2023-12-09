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
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 75;
	private static Company company;

	public static ArrayList<Item> getItems(Company company) {
		CompanyController.company = company;
		List<Item> itemsList = getItemsList();
		ArrayList<Item> res = new ArrayList<>(itemsList);
		return res;
	}

	private static List<Item> getItemsList() {

		return List.of(Item.of("Hire new Employee", CompanyController::addEmployee),
				Item.of("Fire Employee", CompanyController::removeEmployee),
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
		long id = getID(io);
		String name = io.readString("Enter name", "Wrong name", str -> str.matches("[A-Z][a-z]{2,}"));
		String department = getDepartment(io);
		int salary = getSalary(io);
		LocalDate birthDate = io.readIsoDate("Enter birtdate in ISO format  YYYY-MM-DD", "Wrong birthdate",
				LocalDate.now().minusYears(MAX_AGE), LocalDate.now().minusYears(MIN_AGE).minusDays(1));
		Employee empl = new Employee(id, name, department, salary, birthDate);
		boolean res = company.addEmployee(empl);
		io.writeLine(res ? "Employee has been added" : "Employee already exists");
	}

	static void removeEmployee(InputOutput io) {
		long id = getID(io);
		Employee res = company.removeEmployee(id);
		writeResut(io, res, "Employee has been removed");
	}

	static void getEmployee(InputOutput io) {
		long id = getID(io);
		Employee res = company.getEmployee(id);
		writeResut(io, res, res.toString());
	}

	static void getEmployees(InputOutput io) {
		List<Employee> employees = company.getEmployees();
		displayListResult(io, employees);
	}

	static void getDepartmentSalaryDistribution(InputOutput io) {
		List<DepartmentSalary> salary = company.getDepartmentSalaryDistribution();
		displayListResult(io, salary);
	}

	static void getSalaryDistribution(InputOutput io) {
		int interval = io.readInt("Enter interval", "Wrong value");
		List<SalaryDistribution> salary = company.getSalaryDistribution(interval);
		displayListResult(io, salary);

	}

	static void getEmployeesByDepartment(InputOutput io) {
		String dep = getDepartment(io);
		List<Employee> listOfEmpl = company.getEmployeesByDepartment(dep);
		displayListResult(io, listOfEmpl);
	}

	static void getEmployeesBySalary(InputOutput io) {
		int min = getSalary(io);
		int max = getSalary(io);
		List<Employee> listOfEmpl = company.getEmployeesBySalary(min, max);
		displayListResult(io, listOfEmpl);
	}

	static void getEmployeesByAge(InputOutput io) {
		int minAge = io.readInt("Enter min age in years ", "Wrong value", MIN_AGE, MAX_AGE);
		int maxAge = io.readInt("Enter max age in years ", "Wrong value", minAge, MAX_AGE);
		List<Employee> listOfEmpl = company.getEmployeesByAge(minAge, maxAge);
		displayListResult(io, listOfEmpl);
	}

	static void updateSalary(InputOutput io) {
		long id = getID(io);
		int newSalary = getSalary(io);
		Employee empl = company.updateSalary(id, newSalary);
		writeResut(io, empl, "Employee salary has been updated: " + company.getEmployee(id));
	}

	static void updateDepartment(InputOutput io) {
		long id = getID(io);
		String newDep = getDepartment(io);
		Employee empl = company.updateDepartment(id, newDep);
		writeResut(io, empl, "Employee Department has been updated: " + company.getEmployee(id));
	}

	private static String getDepartment(InputOutput io) {
		return io.readString("Enter new Department" + Arrays.deepToString(DEPARTMENTS), "Wrong department",
				SET_DEPARTMENTS);
	}

	private static long getID(InputOutput io) {
		return io.readLong("Enter employee ID", "Wrong identity", MIN_ID, MAX_ID);
	}

	private static Integer getSalary(InputOutput io) {
		return io.readInt("Enter Salary Value", "Wrong salary", MIN_SALARY, MAX_SALARY);
	}

	private static void writeResut(InputOutput io, Employee res, String message) {
		boolean isNoExist = checkEmployeeExist(res);
		io.writeLine(isNoExist ? "Employee does not exist" : message);
	}

	private static boolean checkEmployeeExist(Employee res) {
		return res == null;
	}

	private static <T> void displayListResult(InputOutput io, List<T> list) {
		if (list.isEmpty()) {
			io.writeLine("No data matching the request");
		} else {
			list.forEach(io::writeObjectLine);
		}
	}
}