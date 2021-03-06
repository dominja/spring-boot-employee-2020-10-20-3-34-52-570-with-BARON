package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.exception.NotFoundException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee create(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public Employee searchById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee ID not Found!"));
    }

    public Employee update(Integer id, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isPresent()) {
            optionalEmployee.get().setSalary(employee.getSalary());
            optionalEmployee.get().setAge(employee.getAge());
            optionalEmployee.get().setGender(employee.getGender());
            optionalEmployee.get().setName(employee.getName());

            return employeeRepository.save(optionalEmployee.get());
        }
        throw new NotFoundException(String.format("Employee with an ID of %s not Found!", id));
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchByGender(String gender) {
        try {
            return employeeRepository.findByGender(gender);
        } catch (NotFoundException exception) {
            throw new NotFoundException("None of the employees are " + gender);
        }
    }

    public List<Employee> getEmployeeByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return employeeRepository.findAll(pageable).toList();
    }
}
