package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTests {
	private static final long ID1 = 123;
	private static final String DEP1 = "dep1";
	private static final int SALARY1 = 10000;
	private static final int YEAR1 = 2000;
	private static final LocalDate DATE1 = LocalDate.ofYearDay(YEAR1, 100);
	private static final long ID2 = 124;
	private static final long ID3 = 125;
	private static final long ID4 = 126;
	private static final long ID5 = 127;
	private static final String DEP2 = "dep2";
	private static final String DEP3 = "dep3";
	private static final int SALARY2 = 5000;
	private static final int SALARY3 = 15000;
	private static final int YEAR2 = 1990;
	private static final LocalDate DATE2 = LocalDate.ofYearDay(YEAR2, 100);
	private static final int YEAR3 = 2003;
	private static final LocalDate DATE3 = LocalDate.ofYearDay(YEAR3, 100);
	private static final long ID_NOT_EXIST = 10000000;
	private static final String TEST_FILE_NAME = "test.data";

	Company company;
	Employee empl1 = new Employee(ID1, "Po", DEP1, SALARY1, DATE1);
	Employee empl2 = new Employee(ID2, "Jo", DEP2, SALARY2, DATE2);
	Employee empl3 = new Employee(ID3, "Bo", DEP1, SALARY1, DATE1);
	Employee empl4 = new Employee(ID4, "Mo", DEP2, SALARY2, DATE2);
	Employee empl5 = new Employee(ID5, "Do", DEP3, SALARY3, DATE3);
	Employee[] employees = { empl1, empl2, empl3, empl4, empl5 };

	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		for (Employee empl : employees) {
			company.addEmployee(empl);
		}
	}

	@Test
	void testAddEmployee() {
		assertTrue(company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, SALARY1, DATE1)));
		assertFalse(company.addEmployee(empl1));
	}

	@Test
	void testRemoveEmployee() {
		assertNull(company.removeEmployee(ID_NOT_EXIST));
		assertEquals(empl1, company.removeEmployee(ID1));
		Employee[] expected = { empl2, empl3, empl4, empl5 };
		Employee[] actual = company.getEmployees().toArray(Employee[]::new);
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, actual);
	}

	@Test
	void testGetEmployee() {
		assertNull(company.getEmployee(ID_NOT_EXIST));
		assertEquals(empl1, company.getEmployee(ID1));
	}

	@Test
	void testGetEmployees() {
		Company company2 = new CompanyImpl();
		Employee[] expected2 = {};
		Employee[] actual2 = company2.getEmployees().toArray(Employee[]::new);
		assertArrayEquals(expected2, actual2);

		Employee[] expected = { empl1, empl2, empl3, empl4, empl5 };
		Employee[] actual = company.getEmployees().toArray(Employee[]::new);
		// Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));
		Arrays.sort(actual, Comparator.comparingLong(Employee::id));
		assertArrayEquals(expected, actual);
	}

	@Test
	@Order(1)
	void testSave() {
		company.save(TEST_FILE_NAME);
		assertTrue(Files.exists(Path.of(TEST_FILE_NAME)));
	}

	@Test
	@Order(2)
	void testRestore() {
		Company company2 = new CompanyImpl();
		company2.restore(TEST_FILE_NAME);
		Employee[] expected = { empl1, empl2, empl3, empl4, empl5 };
		Employee[] actual = company2.getEmployees().toArray(Employee[]::new);
		Arrays.sort(actual, Comparator.comparingLong(Employee::id));
		assertArrayEquals(expected, actual);

	}

	// Tests of CW/HW #34
	@Test
	void testGetDepartmentSalaryDistribution() {
		DepartmentSalary[] expectedArr = { new DepartmentSalary(DEP2, SALARY2), new DepartmentSalary(DEP1, SALARY1),
				new DepartmentSalary(DEP3, SALARY3) };
		DepartmentSalary[] actualArr = company.getDepartmentSalaryDistribution().stream()
				.sorted((deps1, deps2) -> Double.compare(deps1.salary(), deps2.salary()))
				.toArray(DepartmentSalary[]::new);
		assertArrayEquals(expectedArr, actualArr);
	}

	@Test
	void testGetSalaryDistribution() {
		company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, 9999, DATE1));
		SalaryDistribution sd1 = new SalaryDistribution(5000, 10000, 3);
		SalaryDistribution sd2 = new SalaryDistribution(10000, 15000, 2);
		SalaryDistribution sd3 = new SalaryDistribution(15000, 20000, 1);
		List<SalaryDistribution> expected = List.of(sd1, sd2, sd3);
		List<SalaryDistribution> actual = company.getSalaryDistribution(5000);
		assertIterableEquals(expected, actual);
	}

	@Test
	void testGetEmployeesByDepartment() {
		runGetByDepartmentTest("XXX", new Employee[0]);
		runGetByDepartmentTest(DEP1, new Employee[] { empl1, empl3 });
	}

	private void runGetByDepartmentTest(String department, Employee[] expected) {
		List<Employee> employees = company.getEmployeesByDepartment(department);
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));

	}

	@Test
	void testGetEmployeesBySalary() {
		runGetBySalaryTest(SALARY2, SALARY3 + 1, employees);
		runGetBySalaryTest(SALARY3 + 1, 100000000, new Employee[0]);
		runGetBySalaryTest(SALARY2, SALARY1 + 1, new Employee[] { empl1, empl2, empl3, empl4 });
	}

	private void runGetBySalaryTest(int salaryFrom, int salaryTo, Employee[] expected) {
		List<Employee> employees = new LinkedList<>(company.getEmployeesBySalary(salaryFrom, salaryTo));
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));
	}

	@Test
	void testGetEmployeesByAge() {
		runGetByAgeTest(getAge(DATE1), getAge(DATE2) + 1, new Employee[] { empl1, empl2, empl3, empl4 });
		runGetByAgeTest(75, 80, new Employee[] {});
	}

	private void runGetByAgeTest(int ageFrom, int ageTo, Employee[] expected) {
		List<Employee> employees = new LinkedList<>(company.getEmployeesByAge(ageFrom, ageTo));
		employees.sort((e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, employees.toArray(Employee[]::new));
	}

	private int getAge(LocalDate birthDate) {

		return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}

	@Test
	void testUpdateSalary() {
		company.addEmployee(new Employee(ID_NOT_EXIST, "name", "depo", SALARY1, DATE1));
		assertEquals(SALARY1, company.getEmployee(ID_NOT_EXIST).salary());
		company.updateSalary(ID_NOT_EXIST, SALARY2);
		assertEquals(SALARY2, company.getEmployee(ID_NOT_EXIST).salary());
		assertEquals(SALARY2, company.getEmployeesByDepartment("depo").get(0).salary());

	}

	@Test
	void testUpdateDepartment() {
		company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, 1000, DATE1));
		assertEquals(DEP1, company.getEmployee(ID_NOT_EXIST).department());
		company.updateDepartment(ID_NOT_EXIST, DEP2);
		assertEquals(DEP2, company.getEmployee(ID_NOT_EXIST).department());
		assertEquals(DEP2, company.getEmployeesBySalary(1000, 2000).get(0).department());
	}

}