package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repositories.EmployeeRepositoryLegacy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private EmployeeRepositoryLegacy employeeRepository;

    public EmployeeService(EmployeeRepositoryLegacy employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.getAll();
    }

    public Employee create(Employee newEmployee) {
        return employeeRepository.create(newEmployee);
    }

    public Employee searchById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee update(Integer id, Employee employee) {
        return employeeRepository.update(id, employee);
    }

    public void delete(Integer id) {
        employeeRepository.delete(id);
    }

    public List<Employee> searchByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    public List<Employee> getEmployeeByPageAndPageSize(int page, int pageSize) {
        return employeeRepository.getAll().stream()
                .skip(pageSize * (page - 1))
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}
