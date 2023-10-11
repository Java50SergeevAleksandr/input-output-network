package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.Employee;
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
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));
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
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.id(), e2.id()));
		assertArrayEquals(expected, actual);
		
	}

}