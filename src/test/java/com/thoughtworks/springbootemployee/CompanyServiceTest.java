package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repositories.CompanyRepositoryLegacy;
import com.thoughtworks.springbootemployee.services.CompanyService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    @Test
    void should_return_2_companies_when_get_companies_given_2_companies() {
        //given
        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.getAll()).thenReturn(asList(new Company(1, "OOCL"), new Company(2, "SM")));
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Integer companyCount = companyService.getAll().size();

        //then
        assertEquals(2, companyCount);
    }

    @Test
    void should_return_company_with_name_OOCL_when_create_given_company_with_name_OOCL() {
        //given
        Company newCompany = new Company(1, "OOCL");
        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.create(newCompany)).thenReturn(newCompany);
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Company company = companyService.create(newCompany);

        //then
        assertEquals("OOCL", company.getCompanyName());
        assertSame(newCompany, company);
    }

    @Test
    void should_return_company_with_name_OOCL_employees_2_employeesNumber_2_when_create_given_company_with_name_OOCL_employees_2() {
        //given
        Company newCompany = new Company(1, "OOCL");
        newCompany.addEmployee(new Employee());
        newCompany.addEmployee(new Employee());

        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.create(newCompany)).thenReturn(newCompany);
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Company company = companyService.create(newCompany);

        //then
        assertEquals("OOCL", company.getCompanyName());
        assertSame(newCompany, company);
        assertEquals(2, company.getEmployeeNumber());
    }

    @Test
    void should_return_company_when_searchByCompanyId_given_company_with_id_of_1() {
        //given
        Company company = new Company(1, "OOCL");
        CompanyRepositoryLegacy repository = mock(CompanyRepositoryLegacy.class);
        when(repository.findById(company.getId())).thenReturn(company);
        CompanyService companyService = new CompanyService(repository);

        //when
        Company fetchedCompany = companyService.searchById(company.getId());

        //then
        assertNotNull(company);
        assertSame(company, fetchedCompany);
    }

    @Test
    void should_return_2_employee_under_OOCL_when_get_employees_given_2_employees() {
        //given
        Company newCompany = new Company(1, "OOCL");
        Employee firstEmployee = new Employee();
        Employee secondEmployee = new Employee();
        newCompany.addEmployee(firstEmployee);
        newCompany.addEmployee(secondEmployee);

        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.getAll()).thenReturn(asList(newCompany));
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        List<Employee> employees = companyService.getEmployeesByCompanyId(newCompany.getId());

        //then
        assertEquals(2, employees.size());
        assertSame(firstEmployee, employees.get(0));
        assertSame(secondEmployee, employees.get(1));
    }

    @Test
    void should_return_updated_company_with_retained_employee_when_update_given_company_with_two_employees_and_field_updates() {
        //given
        Company company = new Company(1, "OOCL");
        Employee firstEmployee = new Employee();
        Employee secondEmployee = new Employee();
        company.addEmployee(firstEmployee);
        company.addEmployee(secondEmployee);

        Company expectedCompany = new Company(1, "OOIL");
        expectedCompany.addEmployee(firstEmployee);
        expectedCompany.addEmployee(secondEmployee);

        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.update(company.getId(), expectedCompany)).thenReturn(expectedCompany);
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Company updatedCompany = companyService.update(company.getId(), expectedCompany);

        //then
        assertSame(expectedCompany, updatedCompany);
        assertEquals(2, updatedCompany.getEmployeeNumber());
    }

    @Test
    void should_trigger_repository_delete_once_when_service_delete_called_given_company_id() {
        //given
        Company company = new Company(1, "OOCL");
        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);

        CompanyService companyService = new CompanyService(companyRepository);

        //when
        companyService.delete(company.getId());

        //then
        verify(companyRepository, times(1)).delete(1);
    }

    @Test
    void should_return_2_companies_when_getCompaniesByPageAndPageSize_given_2_companies_page_1_and_pageSize_2() {
        //given
        Company firstCompany = new Company(1, "OOCL");
        Company secondCompany = new Company(1, "OOIL");
        int page = 1, pageSize = 2;
        CompanyRepositoryLegacy companyRepository = mock(CompanyRepositoryLegacy.class);
        when(companyRepository.getAll()).thenReturn(asList(firstCompany, secondCompany));
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        List<Company> fetchedCompanies = companyService.getCompaniesByPageAndPageSize(page, pageSize);

        //then
        assertEquals(2, fetchedCompanies.size());

    }
}