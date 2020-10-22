package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repositories.CompanyRepositoryLegacy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private CompanyRepositoryLegacy companyRepository;

    public CompanyService(CompanyRepositoryLegacy companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAll() {
        return companyRepository.getAll();
    }

    public Company create(Company newCompany) {
        return companyRepository.create(newCompany);
    }

    public Company searchById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> getEmployeesByCompanyId(Integer id) {
        return companyRepository.getAll()
                .stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .map(Company::getEmployees)
                .orElse(Collections.emptyList());
    }

    public Company update(Integer id, Company expectedCompany) {
        return companyRepository.update(id, expectedCompany);
    }

    public void delete(Integer id) {
        companyRepository.delete(id);
    }

    public List<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {
        return companyRepository.getAll().stream()
                .skip(pageSize * (page - 1))
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}
