package telran.employees.net;

import java.util.List;

import telran.employees.CompanyProtocol;
import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.dto.UpdateDepartmentData;
import telran.employees.dto.UpdateSalaryData;
import telran.employees.service.Company;
import telran.net.NetworkHandler;

public class CompanyNetworkProxy implements Company {
	private NetworkHandler networkHandler;

	public CompanyNetworkProxy(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	@Override
	public boolean addEmployee(Employee empl) {
		return networkHandler.send(CompanyProtocol.employeeAdd(), empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		return networkHandler.send(CompanyProtocol.employeeRemove(), id);
	}

	@Override
	public Employee getEmployee(long id) {
		return networkHandler.send(CompanyProtocol.employeeGet(), id);
	}

	@Override
	public List<Employee> getEmployees() {
		return networkHandler.send(CompanyProtocol.employeesAll(), null);
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		return networkHandler.send(CompanyProtocol.employeesDepartmentSalaryDistribution(), null);
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		return networkHandler.send(CompanyProtocol.employeesSalaryDistribution(), interval);
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		return networkHandler.send(CompanyProtocol.employeesGetByDepartment(), department);
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		return networkHandler.send(CompanyProtocol.employeesGetBySalary(), new int[] { salaryFrom, salaryTo });
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		return networkHandler.send(CompanyProtocol.employeesGetByAge(), new int[] { ageFrom, ageTo });
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		return networkHandler.send(CompanyProtocol.employeeSalaryUpdate(), new UpdateSalaryData(id, newSalary));
	}

	@Override
	public Employee updateDepartment(long id, String department) {
		return networkHandler.send(CompanyProtocol.employeesDepartmentUpdate(),
				new UpdateDepartmentData(id, department));
	}

}
