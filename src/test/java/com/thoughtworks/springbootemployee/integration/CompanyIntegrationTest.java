package com.thoughtworks.springbootemployee.integration;

import com.google.gson.Gson;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyIntegrationTest {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    private final Gson gson = new Gson();

    @Test
    void should_return_all_companies_when_called_get_all() throws Exception {
        //given
        Company company = new Company("00CL", Collections.emptyList());
        companyRepository.save(company);

        //when then
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("00CL"))
                .andExpect(jsonPath("$[0].employees").isEmpty());
    }

    @Test
    void should_create_company_when_created() throws Exception {
        //given
        Company company = new Company("00CL", Collections.emptyList());
        String employeeAsJson = gson.toJson(company, Company.class);

        // when then
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeAsJson))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value("00CL"))
                .andExpect(jsonPath("$.employees").isEmpty());
    }

}
