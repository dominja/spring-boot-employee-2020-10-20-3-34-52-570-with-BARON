package com.thoughtworks.springbootemployee;

import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import com.thoughtworks.springbootemployee.services.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);

        List<Employee> employeeList = Arrays.asList(
                new Employee("nelly", 18, "female", 10),
                new Employee("nelly", 18, "male", 10),
                new Employee("nelly", 18, "male", 10));

        List<Employee> employeeList2 = Arrays.asList(
                new Employee("nelly", 18, "female", 10),
                new Employee("nelly", 18, "male", 10),
                new Employee("nelly", 18, "male", 10));

        when(companyRepository.findAll()).thenReturn(asList(new Company("OOCL", employeeList),
                new Company("SM", employeeList2)));

        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Integer companyCount = companyService.getAll().size();

        //then
        assertEquals(2, companyCount);
    }

    @Test
    void should_return_company_with_name_OOCL_when_create_given_company_with_name_OOCL() {
        //given
        List<Employee> employeeList = Arrays.asList(
                new Employee("nelly", 18, "female", 10),
                new Employee("nelly", 18, "male", 10),
                new Employee("nelly", 18, "male", 10));

        Company newCompany = new Company("OOCL", employeeList);
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);
        when(companyRepository.save(newCompany)).thenReturn(newCompany);
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
        List<Employee> employeeList = Arrays.asList(
                new Employee("nelly", 18, "female", 10),
                new Employee("nelly", 18, "male", 10),
                new Employee("nelly", 18, "male", 10));

        Company newCompany = new Company("OOCL", employeeList);

        ICompanyRepository companyRepository = mock(ICompanyRepository.class);
        when(companyRepository.save(newCompany)).thenReturn(newCompany);
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Company company = companyService.create(newCompany);

        //then
        assertEquals("OOCL", company.getCompanyName());
        assertSame(newCompany, company);
        assertEquals(3, company.getEmployeeNumber());
    }

    @Test
    void should_return_company_when_searchByCompanyId_given_company_with_id_of_1() {
        //given
        List<Employee> employeeList = Arrays.asList(
                new Employee("nelly", 18, "female", 10),
                new Employee("nelly", 18, "male", 10),
                new Employee("nelly", 18, "male", 10));

        Company company = new Company("OOCL", employeeList);
        ICompanyRepository repository = mock(ICompanyRepository.class);
        when(repository.findById(company.getId())).thenReturn(Optional.of(company));
        CompanyService companyService = new CompanyService(repository);

        //when
        Optional<Company> fetchedCompany = companyService.searchById(company.getId());

        //then
        assertNotNull(company);
        assertSame(company, fetchedCompany.orElse(null));
    }

    @Test
    void should_return_2_employee_under_OOCL_when_get_employees_given_2_employees() {
        //given

        Employee firstEmployee = new Employee("nelly", 18, "female", 10);
        Employee secondEmployee = new Employee("nelly", 18, "female", 10);
        Company newCompany = new Company("OOCL", Arrays.asList(firstEmployee, secondEmployee));
        newCompany.setId(1);
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);
        when(companyRepository.findById(1)).thenReturn(Optional.of(newCompany));
        CompanyService companyService = new CompanyService(companyRepository);
        //when
        List<Employee> employees = companyService.getEmployeesByCompanyId(1);

        //then
        assertEquals(2, employees.size());
        assertSame(firstEmployee, employees.get(0));
        assertSame(secondEmployee, employees.get(1));
    }

    @Test
    void should_return_updated_company_with_retained_employee_when_update_given_company_with_two_employees_and_field_updates() {
        //given
        Company company = new Company("OOCL", null);
        company.setId(1);
        Company expectedCompany = new Company("OOIL", null);
        expectedCompany.setId(1);
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(expectedCompany);
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        Company updatedCompany = companyService.update(company.getId(), expectedCompany);

        //then
        assertSame(expectedCompany, updatedCompany);
    }

    @Test
    void should_trigger_repository_delete_once_when_service_delete_called_given_company_id() {
        //given
        Company company = new Company("OOCL", null);
        company.setId(1);
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);

        CompanyService companyService = new CompanyService(companyRepository);

        //when
        companyService.delete(company.getId());

        //then
        verify(companyRepository, times(1)).deleteById(1);
    }

    @Test
    void should_return_2_companies_when_getCompaniesByPageAndPageSize_given_2_companies_page_1_and_pageSize_2() {
        //given
        Company firstCompany = new Company("OOCL", null);
        Company secondCompany = new Company("OOIL", null);
        int page = 1, pageSize = 2;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        ICompanyRepository companyRepository = mock(ICompanyRepository.class);
        Page<Company> mockPage = mock(Page.class);
        when(companyRepository.findAll(pageable)).thenReturn(mockPage);
        when(companyRepository.findAll(pageable).toList()).thenReturn(asList(firstCompany, secondCompany));
        CompanyService companyService = new CompanyService(companyRepository);

        //when
        List<Company> fetchedCompanies = companyService.getCompaniesByPageAndPageSize(page, pageSize);

        //then
        assertEquals(2, fetchedCompanies.size());

    }
}