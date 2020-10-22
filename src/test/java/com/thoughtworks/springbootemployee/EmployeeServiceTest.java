package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repositories.EmployeeRepositoryLegacy;
import com.thoughtworks.springbootemployee.services.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    @Test
    void should_return_2_when_get_employees_given_2_employees() {
        //given
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.getAll()).thenReturn(asList(new Employee(), new Employee()));
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        Integer employeeCount = employeeService.getAll().size();

        //then
        assertEquals(2, employeeCount);
    }

    @Test
    void should_return_employee_with_id_1_when_create_given_employee_with_id_of_1() {
        //given
        Employee newEmployee = new Employee(1, "", 20, "male", 1000);
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.create(newEmployee)).thenReturn(newEmployee);
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        Employee employee = employeeService.create(newEmployee);

        //then
        assertEquals(1, employee.getId());
    }

    @Test
    void should_return_employee_when_searchById_given_employee_with_id_of_1() {
        //given
        Employee employee = new Employee(1, "", 20, "male", 1000);
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.findById(employee.getId())).thenReturn(employee);
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        Employee fetchedEmployee = employeeService.searchById(employee.getId());

        //then
        assertNotNull(employee);
        assertSame(employee, fetchedEmployee);
    }

    @Test
    void should_return_updated_employee_when_update_given_employee_and_field_updates() {
        //given
        Employee employee = new Employee(1, "", 20, "male", 1000);
        Employee expectedEmployee = new Employee(1, "Cedric", 19, "female", 6600);
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.update(employee.getId(), employee)).thenReturn(expectedEmployee);
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        Employee updatedEmployee = employeeService.update(employee.getId(), employee);

        //then
        assertSame(expectedEmployee, updatedEmployee);
    }

    @Test
    void should_trigger_repository_delete_once_when_service_delete_called_given_1_employee() {
        //given
        Employee employee = new Employee(1, "", 20, "male", 1000);
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);

        EmployeeService employeeService = new EmployeeService(repository);
        //when
        employeeService.delete(employee.getId());
        //then
        verify(repository, times(1)).delete(1);
    }

    @Test
    void should_return_2_male_employee_when_getByGender_given_2_male_1_female() {
        //given
        Employee firstEmployee = new Employee(1, "Cedric", 20, "male", 1000);
        Employee secondEmployee = new Employee(2, "Jaycee", 20, "male", 1000);
        Employee thirdEmployee = new Employee(3, "Lisa", 20, "female", 1000);
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.findByGender("male")).thenReturn(asList(firstEmployee, secondEmployee));
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        List<Employee> employees = employeeService.searchByGender("male");

        //then
        assertEquals(2, employees.size());
    }

    @Test
    void should_return_2_employees_when_getByEmployeeByPage_given_2_employees_page_1_and_pageSize_2() {
        //given
        Employee firstEmployee = new Employee(1, "Cedric", 20, "male", 1000);
        Employee secondEmployee = new Employee(2, "Jaycee", 20, "male", 1000);
        int page = 1, pageSize = 2;
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.getAll()).thenReturn(asList(firstEmployee, secondEmployee));
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        List<Employee> fetchedEmployees = employeeService.getEmployeeByPageAndPageSize(page, pageSize);

        //then
        assertEquals(2, fetchedEmployees.size());

    }

    @Test
    void should_return_2_employees_when_getByEmployeeByPage_given_2_employees_page_1_and_pageSize_5() {
        //given
        Employee firstEmployee = new Employee(1, "Cedric", 20, "male", 1000);
        Employee secondEmployee = new Employee(2, "Jaycee", 20, "male", 1000);
        int page = 1, pageSize = 5;
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.getAll()).thenReturn(asList(firstEmployee, secondEmployee));
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        List<Employee> fetchedEmployees = employeeService.getEmployeeByPageAndPageSize(page, pageSize);

        //then
        assertEquals(2, fetchedEmployees.size());

    }

    @Test
    void should_return_third_employee_when_getByEmployeeByPage_given_3_employees_page_2_and_pageSize_2() {
        //given
        Employee firstEmployee = new Employee(1, "Cedric", 20, "male", 1000);
        Employee secondEmployee = new Employee(2, "Jaycee", 20, "male", 1000);
        Employee thirdEmployee = new Employee(3, "Shana", 20, "male", 1000);
        int page = 2, pageSize = 2;
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.getAll()).thenReturn(asList(firstEmployee, secondEmployee, thirdEmployee));
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        List<Employee> fetchedEmployees = employeeService.getEmployeeByPageAndPageSize(page, pageSize);

        //then
        assertEquals(1, fetchedEmployees.size());
        assertSame(thirdEmployee, fetchedEmployees.get(0));

    }

    @Test
    void should_return_0_employee_when_getByEmployeeByPage_given_0_employees_page_1_and_pageSize_2() {
        //given
        int page = 1, pageSize = 2;
        EmployeeRepositoryLegacy repository = mock(EmployeeRepositoryLegacy.class);
        when(repository.getAll()).thenReturn(Collections.emptyList());
        EmployeeService employeeService = new EmployeeService(repository);

        //when
        List<Employee> fetchedEmployees = employeeService.getEmployeeByPageAndPageSize(page, pageSize);

        //then
        assertEquals(0, fetchedEmployees.size());
    }
}