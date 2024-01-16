package telran.employees;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import telran.employees.dto.*;
import telran.employees.service.Company;
import telran.net.ApplProtocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

@SuppressWarnings("unused")
public class CompanyProtocol implements ApplProtocol {
	private Company company;

	public CompanyProtocol(Company company) {
		this.company = company;
	}

	@Override
	public Response getResponse(Request request) {
		Serializable requestData = request.requestData();
		String requestType = request.requestType();
		Response response = new Response(ResponseCode.WRONG_TYPE, requestType);
		Serializable responseData;
		try {
			Method method = CompanyProtocol.class.getDeclaredMethod(requestType.replace('/', '_'), Serializable.class);
			method.setAccessible(true);
			responseData = (Serializable) method.invoke(this, requestData);
			response = new Response(ResponseCode.OK, responseData);
		} catch (NoSuchMethodException e) {
			System.out.println(e.toString());
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
