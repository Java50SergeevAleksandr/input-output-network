package telran.employees;

import java.io.Serializable;
import java.util.ArrayList;

import telran.employees.dto.*;
import telran.employees.service.Company;
import telran.net.ApplProtocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements ApplProtocol {
	private static final String EMPLOYEE_SALARY_UPDATE = "employee/salary/update";
	private static final String EMPLOYEES_ALL = "employees/all";
	private static final String EMPLOYEE_GET = "employee/get";
	private static final String EMPLOYEE_ADD = "employee/add";
	private static final String WRONG_TYPE = "Wrong type from request";
	private static final String EMPLOYEE_REMOVE = "employee/remove";
	private static final String EMPLOYEE_DEP_SALARY_DISTR = "employee/department/salaryDistribution";
	private static final String EMPLOYEE_SALARY_DISTR = "employee/salary/distribution";
	private static final String EMPLOYEE_GET_BY_DEP = "employee/getBydepartment";
	private static final String EMPLOYEE_GET_BY_SALARY = "employee/getBySalary";
	private static final String EMPLOYEE_GET_BY_AGE = "employee/getByAge";
	private static final String EMPLOYEE_DEP_UPDATE = "employee/department/update";
	private Company company;

	public static String employeeSalaryUpdate() {
		return EMPLOYEE_SALARY_UPDATE;
	}

	public static String employeesAll() {
		return EMPLOYEES_ALL;
	}

	public static String employeeGet() {
		return EMPLOYEE_GET;
	}

	public static String employeeAdd() {
		return EMPLOYEE_ADD;
	}

	public static String employeeRemove() {
		return EMPLOYEE_REMOVE;
	}

	public static String employeesDepartmentSalaryDistribution() {
		return EMPLOYEE_DEP_SALARY_DISTR;
	}

	public static String employeesSalaryDistribution() {
		return EMPLOYEE_SALARY_DISTR;
	}

	public static String employeesGetByDepartment() {
		return EMPLOYEE_GET_BY_DEP;
	}

	public static String employeesGetBySalary() {
		return EMPLOYEE_GET_BY_SALARY;
	}

	public static String employeesGetByAge() {
		return EMPLOYEE_GET_BY_AGE;
	}

	public static String employeesDepartmentUpdate() {
		return EMPLOYEE_DEP_UPDATE;
	}

	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		Serializable requestData = request.requestData();
		String requestType = request.requestType();
		Response response = null;
		Serializable responseData;
		try {
			responseData = switch (requestType) {
			case EMPLOYEE_ADD -> employee_add(requestData);
			case EMPLOYEE_REMOVE -> employee_remove(requestData);
			case EMPLOYEE_GET -> employee_get(requestData);
			case EMPLOYEES_ALL -> employees_all(requestData);
			case EMPLOYEE_SALARY_UPDATE -> employee_salary_update(requestData);
			case EMPLOYEE_DEP_SALARY_DISTR -> employee_department_salaryDistribution(requestData);
			case EMPLOYEE_DEP_UPDATE -> employee_department_update(requestData);
			case EMPLOYEE_GET_BY_AGE -> employee_get_byAge(requestData);
			case EMPLOYEE_GET_BY_SALARY -> employee_get_bySalary(requestData);
			case EMPLOYEE_GET_BY_DEP -> employee_get_byDepartment(requestData);
			case EMPLOYEE_SALARY_DISTR -> employee_salary_distribution(requestData);
			default -> WRONG_TYPE;
			};
			response = responseData == WRONG_TYPE ? new Response(ResponseCode.WRONG_TYPE, requestType)
					: new Response(ResponseCode.OK, responseData);
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
		}

		return response;
	}

	private Serializable employee_salary_distribution(Serializable requestData) {
		return new ArrayList<>(company.getSalaryDistribution((int) requestData));
	}

	private Serializable employee_get_byDepartment(Serializable requestData) {
		return new ArrayList<>(company.getEmployeesByDepartment((String) requestData));
	}

	private Serializable employee_get_bySalary(Serializable requestData) {
		int[] data = (int[]) requestData;
		return new ArrayList<>(company.getEmployeesBySalary(data[0], data[1]));
	}

	private Serializable employee_get_byAge(Serializable requestData) {
		int[] data = (int[]) requestData;
		return new ArrayList<>(company.getEmployeesByAge(data[0], data[1]));
	}

	private Serializable employee_department_update(Serializable requestData) {
		UpdateDepartmentData data = (UpdateDepartmentData) requestData;
		return company.updateDepartment(data.id(), data.department());
	}

	private Serializable employee_department_salaryDistribution(Serializable requestData) {
		return new ArrayList<>(company.getDepartmentSalaryDistribution());
	}

	private Serializable employee_remove(Serializable requestData) {
		return company.removeEmployee((long) requestData);
	}

	private Serializable employee_salary_update(Serializable requestData) {
		UpdateSalaryData data = (UpdateSalaryData) requestData;
		long id = data.id();
		int newSalary = data.newSalary();
		return company.updateSalary(id, newSalary);
	}

	private Serializable employees_all(Serializable requestData) {
		return new ArrayList<>(company.getEmployees());
	}

	private Serializable employee_get(Serializable requestData) {
		long id = (long) requestData;
		return company.getEmployee(id);
	}

	private Serializable employee_add(Serializable requestData) {
		Employee empl = (Employee) requestData;
		return company.addEmployee(empl);
	}

}
