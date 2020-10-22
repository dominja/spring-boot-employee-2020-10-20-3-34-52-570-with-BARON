package com.thoughtworks.springbootemployee.integration;

import com.google.gson.Gson;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EmployeeIntegrationTest {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    private final Gson gson = new Gson();

    @Test
    void should_return_all_employees_when_called_get_all() throws Exception {
        //given
        Employee employee = new Employee("nelly", 18, "female", 10);
        employeeRepository.save(employee);
        // when then
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("nelly"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(10));
    }

    @Test
    void should_create_employees_when_created() throws Exception {
        //given
        Employee employee = new Employee("nelly", 18, "female", 10);
        String employeeAsJson = gson.toJson(employee, Employee.class);

        // when then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeAsJson))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("nelly"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.salary").value(10));
    }

    @Test
    void should_return_updated_employee_when_update_given_employee() throws Exception {
        //given
        Employee employee = new Employee("nelly", 18, "female", 10);
        Employee createdEmployee = employeeRepository.save(employee);
        Employee updatedEmployee = new Employee("yllen", 19, "female", 100);

        // when then
        mockMvc.perform(put("/employees/" + createdEmployee.getId())
                .content(gson.toJson(updatedEmployee, Employee.class))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEmployee.getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }

    @Test
    void should_delete_employees_when_deleted() throws Exception {
        // given
        Employee employee = new Employee("nelly", 18, "female", 10);
        Employee createdEmployee = employeeRepository.save(employee);

        // when
        mockMvc.perform(delete("/employees/" + createdEmployee.getId())).andExpect(status().isOk());

        // then
        Optional<Employee> fetchedEmployee = employeeRepository.findById(employee.getId());
        assertFalse(fetchedEmployee.isPresent());
    }

    @Test
    void should_return_employee_when_search_by_id_given_employeeID() throws Exception {
        //given
        Employee employee = new Employee("nelly", 18, "female", 10);
        Employee createdEmployee = employeeRepository.save(employee);
        // when then
        mockMvc.perform(get("/employees/" + createdEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("nelly"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.salary").value(10));
    }

    @Test
    void should_return_employee_filtered_by_gender_when_search_by_gender_given_gender_female() throws Exception {
        //given
        Employee employee1 = new Employee("nelly", 18, "female", 10);
        Employee employee2 = new Employee( "nelly", 18, "male", 10);
        Employee employee3 = new Employee( "nelly", 18, "male", 10);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        // when then
        mockMvc.perform(get("/employees?gender=female" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("nelly"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(10));
    }

    @Test
    void should_return_2_employee_when_getByEmployeeByPage_given_3_employees_page_1_pageSize_2() throws Exception {
        //given
        Employee employee1 = new Employee( "nelly", 18, "female", 10);
        Employee employee2 = new Employee( "janelle", 18, "female", 10);
        Employee employee3 = new Employee( "cedric", 18, "male", 10);

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        // when then
        mockMvc.perform(get("/employees?page=1&pageSize=2" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("nelly"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(10))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].name").value("janelle"))
                .andExpect(jsonPath("$[1].age").value(18))
                .andExpect(jsonPath("$[1].gender").value("female"))
                .andExpect(jsonPath("$[1].salary").value(10));
    }
}
