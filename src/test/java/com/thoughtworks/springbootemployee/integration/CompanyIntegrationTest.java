package com.thoughtworks.springbootemployee.integration;

import com.google.gson.Gson;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    @Test
    void should_return_2_company_when_findall_using_pagination_given_3_employees_page_1_pageSize_2() throws Exception {
        //given
        Company company1 = new Company("00CL", Collections.emptyList());
        Company company2 = new Company("00CLL", Collections.emptyList());
        Company company3 = new Company("Yangmin", Collections.emptyList());

        companyRepository.save(company1);
        companyRepository.save(company2);
        companyRepository.save(company3);

        // when then
        mockMvc.perform(get("/companies?page=1&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("00CL"))
                .andExpect(jsonPath("$[0].employees").isEmpty())
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].companyName").value("00CLL"))
                .andExpect(jsonPath("$[1].employees").isEmpty());
    }

    @Test
    void should_delete_company_when_deleted() throws Exception {
        // given
        Company company = new Company("00CL", Collections.emptyList());
        Company newCompany=companyRepository.save(company);

        // when
        mockMvc.perform(delete("/companies/" + newCompany.getCompanyId()))
                .andExpect(status().isOk());

        // then
        Optional<Company> fetchedCompany = companyRepository.findById(company.getCompanyId());
        assertFalse(fetchedCompany.isPresent());
    }
     @Test
         void should_return_updated_company_when_update_given_new_company() throws Exception {
             //given
         Employee employee1 = new Employee("nelly", 18, "female", 10);
         Employee employee2 = new Employee("nelly", 18, "male", 10);

         Company company = new Company("00CL", Arrays.asList(employee1,employee2));
         Company newCompany=companyRepository.save(company);
         Company updatedCompanyRequest=new Company("alibaba", Collections.emptyList());

         // when then
         mockMvc.perform(put("/companies/" + newCompany.getCompanyId())
                 .content(gson.toJson(updatedCompanyRequest, Company.class))
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.id").value(newCompany.getCompanyId()))
                 .andExpect(jsonPath("$.companyName").value(updatedCompanyRequest.getCompanyName()))
                 .andExpect(jsonPath("$.employees").isArray());
         }

    @Test
    void should_return_company_when_search_by_id_given_companyID() throws Exception {
        //given
        Company company = new Company("00CL", Collections.emptyList());
        Company newCompany = companyRepository.save(company);
        // when then
        mockMvc.perform(get("/companies/" + newCompany.getCompanyId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value("00CL"))
                .andExpect(jsonPath("$.employees").isEmpty());
    }
}
