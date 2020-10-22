package com.thoughtworks.springbootemployee.repositories;

import com.thoughtworks.springbootemployee.models.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepositoryLegacy {

    private final List<Company> companies = new ArrayList<>();

    public List<Company> getAll() {
        return companies;
    }

    public Company create(Company newCompany) {
        companies.add(newCompany);
        return newCompany;
    }

    public Company findById(Integer id) {
        return companies.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Company update(Integer id, Company updatedCompany) {
        companies.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst().ifPresent(company -> {
                    company.setId(updatedCompany.getId());
                    company.setCompanyName(updatedCompany.getCompanyName());
                });
        return updatedCompany;
    }

    public void delete(Integer companyId) {
        companies.stream()
                .filter(company -> company.getId().equals(companyId))
                .findFirst()
                .ifPresent(companies::remove);
    }
}
