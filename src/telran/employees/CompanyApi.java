package telran.employees;

public interface CompanyApi {
	static final int SERVER_PORT = 5000;
	static final String EMPLOYEE_SALARY_UPDATE = "employee/salary/update";
	static final String EMPLOYEES_ALL = "employees/all";
	static final String EMPLOYEE_GET = "employee/get";
	static final String EMPLOYEE_ADD = "employee/add";
	static final String WRONG_TYPE = "Wrong type from request";
	static final String EMPLOYEE_REMOVE = "employee/remove";
	static final String EMPLOYEE_DEP_SALARY_DISTR = "employee/department/salaryDistribution";
	static final String EMPLOYEE_SALARY_DISTR = "employee/salary/distribution";
	static final String EMPLOYEE_GET_BY_DEP = "employee/getBydepartment";
	static final String EMPLOYEE_GET_BY_SALARY = "employee/getBySalary";
	static final String EMPLOYEE_GET_BY_AGE = "employee/getByAge";
	static final String EMPLOYEE_DEP_UPDATE = "employee/department/update";
}
