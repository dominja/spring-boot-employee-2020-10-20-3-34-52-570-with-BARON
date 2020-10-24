package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.exception.NotFoundException;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import com.thoughtworks.springbootemployee.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private static final String COMPANY_ID_S_DOES_NOT_EXIST = "Company ID %s does not exist!";
    private ICompanyRepository companyRepository;
    private IEmployeeRepository employeeRepository;

    public CompanyService(ICompanyRepository companyRepository, IEmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company create(Company newCompany) {
        return companyRepository.save(newCompany);
    }

    public Company searchById(Integer id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(COMPANY_ID_S_DOES_NOT_EXIST, id)));
    }

    public List<Employee> getEmployeesByCompanyId(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get().getEmployees();
        }
        throw new NotFoundException(String.format(COMPANY_ID_S_DOES_NOT_EXIST, id));
    }

    public Company update(Integer id, Company updatedCompany) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            optionalCompany.get().setCompanyName(updatedCompany.getCompanyName());
            List<Employee> updatedEmployees = updatedCompany.getEmployees();
            updateEmployee(updatedEmployees);
            return companyRepository.save(optionalCompany.get());
        }
        throw new NotFoundException(String.format(COMPANY_ID_S_DOES_NOT_EXIST, id));
    }

    private void updateEmployee(List<Employee> updatedEmployees) {
        if (updatedEmployees != null) {
            updatedEmployees.forEach(employee -> employeeRepository.save(employee));
        }
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }

    public List<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return companyRepository.findAll(pageable).toList();
    }
}
