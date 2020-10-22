package com.thoughtworks.springbootemployee.services;

import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.models.Employee;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company create(Company newCompany) {
        return companyRepository.save(newCompany);
    }

    public Optional<Company> searchById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> getEmployeesByCompanyId(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get().getEmployees();
        }
        return Collections.emptyList();
    }

    public Company update(Integer id, Company updatedCompany) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            optionalCompany.get().setCompanyName(updatedCompany.getCompanyName());
            return companyRepository.save(optionalCompany.get());
        }
        return null;
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }

    public List<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return companyRepository.findAll(pageable).toList();
    }
}
