package com.thoughtworks.springbootemployee.repositories;

import com.thoughtworks.springbootemployee.models.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryLegacy {
    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getAll() {
        return employees;
    }

    public Employee create(Employee newEmployee) {
        employees.add(newEmployee);
        return newEmployee;
    }

    public Employee findById(Integer id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Employee update(Integer id, Employee updatedEmployee) {
        employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst().ifPresent(employee -> {
            employees.remove(employee);
            employees.add(updatedEmployee);
        });
        return updatedEmployee;
    }

    public void delete(Integer id) {
        employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst().ifPresent(employees::remove);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }
}
