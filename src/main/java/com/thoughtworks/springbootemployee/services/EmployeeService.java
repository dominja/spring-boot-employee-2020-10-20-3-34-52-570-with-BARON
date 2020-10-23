package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.dto.EmployeeRequest;
import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private IEmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;

    public EmployeeService(IEmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee create(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public Optional<Employee> searchById(Integer id) {
        return employeeRepository.findById(id);
    }

    public EmployeeResponse update(Integer id, EmployeeRequest employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee updatedEmployee;

        if (optionalEmployee.isPresent()) {
            optionalEmployee.get().setSalary(employee.getSalary());
            optionalEmployee.get().setAge(employee.getAge());
            optionalEmployee.get().setGender(employee.getGender());
            optionalEmployee.get().setName(employee.getName());

            employeeRepository.save(optionalEmployee.get());
            updatedEmployee = employeeRepository.save(employeeMapper.toEntity(employee));
            return employeeMapper.toResponse(updatedEmployee);
        }
        return null;
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> searchByGender(String gender) {
        List<Employee> employeeList = employeeRepository.findByGender(gender);
        return employeeList.stream().map(employeeMapper::toResponse).collect(Collectors.toList());
    }

    public List<EmployeeResponse> getEmployeeByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<Employee> employeeList = employeeRepository.findAll(pageable).toList();
        return employeeList.stream().map(employeeMapper::toResponse).collect(Collectors.toList());
    }
}
