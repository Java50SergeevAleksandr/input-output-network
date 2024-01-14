package telran.employees.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;

public class CompanyImpl implements Company {
	HashMap<Long, Employee> employees = new HashMap<>(); // most effective structure for the interface methods
	TreeMap<LocalDate, Set<Employee>> employeesByAge = new TreeMap<>();
	TreeMap<Integer, Set<Employee>> employeesBySalary = new TreeMap<>();
	HashMap<String, Set<Employee>> employeesByDepartment = new HashMap<>();
	ReentrantReadWriteLock RWLock = new ReentrantReadWriteLock();
	Lock readLock = RWLock.readLock();
	Lock writeLock = RWLock.writeLock();

	@Override
	public boolean addEmployee(Employee empl) {
		try {
			writeLock.lock();
			boolean res = employees.putIfAbsent(empl.id(), empl) == null;
			if (res) {
				addToIndex(empl, employeesByAge, Employee::birthDate);
				addToIndex(empl, employeesBySalary, Employee::salary);
				addToIndex(empl, employeesByDepartment, Employee::department);
			}

			return res;
		} finally {
			writeLock.unlock();
		}

	}

	private <K> void addToIndex(Employee empl, Map<K, Set<Employee>> multiMap, Function<Employee, K> keyExtractor) {
		multiMap.computeIfAbsent(keyExtractor.apply(empl), k -> new HashSet<>()).add(empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		try {
			writeLock.lock();
			Employee empl = employees.remove(id);
			if (empl != null) {
				removeFromIndex(empl, employeesByAge, Employee::birthDate);
				removeFromIndex(empl, employeesBySalary, Employee::salary);
				removeFromIndex(empl, employeesByDepartment, Employee::department);
			}

			return empl;
		} finally {
			writeLock.unlock();
		}

	}

	private <K> void removeFromIndex(Employee empl, Map<K, Set<Employee>> multiMap,
			Function<Employee, K> keyExtractor) {
		multiMap.computeIfPresent(keyExtractor.apply(empl), (k, c) -> {
			c.remove(empl);
			return c.isEmpty() ? null : c;
		});
	}

	@Override
	public Employee getEmployee(long id) {
		try {
			readLock.lock();
			return employees.get(id);
		} finally {
			readLock.unlock();
		}

	}

	// ShallowCopy
	@Override
	public List<Employee> getEmployees() {
		try {
			readLock.lock();
			return employees.values().stream().sorted(Comparator.comparingLong(Employee::id)).toList();
			// return new ArrayList<>(employees.values());
		} finally {
			readLock.unlock();
		}
	}

	// Unmodifiable view
	public Collection<Employee> getEmployeesView() {
		try {
			readLock.lock();
			return Collections.unmodifiableCollection(employees.values());
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		try {
			readLock.lock();
			return employeesByDepartment.entrySet().stream()
					.map(d -> new DepartmentSalary(d.getKey(),
							d.getValue().stream().collect(Collectors.averagingDouble(e -> e.salary()))))
					.sorted(Comparator.comparingDouble(DepartmentSalary::salary)).toList();
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		try {
			readLock.lock();
			Map<Integer, Long> mapIntervalNumbers = employees.values().stream()
					.collect(Collectors.groupingBy(e -> e.salary() / interval, Collectors.counting()));
			return mapIntervalNumbers
					.entrySet().stream().map(e -> new SalaryDistribution(e.getKey() * interval,
							e.getKey() * interval + interval, e.getValue().intValue()))
					.sorted(Comparator.comparingInt(SalaryDistribution::min)).toList();
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		try {
			readLock.lock();
			Collection<Employee> employeesCol = employeesByDepartment.get(department);
			ArrayList<Employee> res = new ArrayList<>();
			if (employeesCol != null) {
				res.addAll(employeesCol);
			}
			return res;
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		try {
			readLock.lock();
			return employeesBySalary.subMap(salaryFrom, salaryTo).values().stream().flatMap(Set::stream).toList();
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		try {
			readLock.lock();
			LocalDate dateTo = getDate(ageFrom);
			LocalDate dateFrom = getDate(ageTo);
			return employeesByAge.subMap(dateFrom, dateTo).values().stream().flatMap(Set::stream).toList();
		} finally {
			readLock.unlock();
		}
	}

	private LocalDate getDate(int ageFrom) {
		return LocalDate.now().minusYears(ageFrom);
	}

	private int getAge(LocalDate birthDate) {
		return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		try {
			writeLock.lock();
			Employee empl = removeEmployee(id);
			if (empl != null) {
				Employee newEmpl = new Employee(id, empl.name(), empl.department(), newSalary, empl.birthDate());
				addEmployee(newEmpl);
			}
			return empl;
		} finally {
			writeLock.unlock();
		}

	}

	@Override
	public Employee updateDepartment(long id, String department) {
		try {
			writeLock.lock();
			Employee empl = removeEmployee(id);
			if (empl != null) {
				Employee newEmpl = new Employee(id, empl.name(), department, empl.salary(), empl.birthDate());
				addEmployee(newEmpl);
			}
			return empl;
		} finally {
			writeLock.unlock();
		}
	}
}