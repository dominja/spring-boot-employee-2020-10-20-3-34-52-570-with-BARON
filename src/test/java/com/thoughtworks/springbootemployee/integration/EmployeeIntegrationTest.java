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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        Employee employee = new Employee(1, "nelly", 18, "female", 10);
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
        Employee employee = new Employee(1, "nelly", 18, "female", 10);
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
        Employee employee = new Employee(1, "nelly", 18, "female", 10);
        Employee createdEmployee = employeeRepository.save(employee);
        Employee updatedEmployee = new Employee(createdEmployee.getId(), "yllen", 19, "female", 100);

        // when then
        mockMvc.perform(put("/employees/" + createdEmployee.getId())
                .content(gson.toJson(updatedEmployee, Employee.class))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(jsonPath("$.name").value(updatedEmployee.getName()))
                .andExpect(jsonPath("$.age").value(updatedEmployee.getAge()))
                .andExpect(jsonPath("$.gender").value(updatedEmployee.getGender()))
                .andExpect(jsonPath("$.salary").value(updatedEmployee.getSalary()));
    }
}
