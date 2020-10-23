package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private CompanyService companyService;

    public CompaniesController(CompanyService companyService) {
        this.companyService = companyService;

    }

    @GetMapping
    public List<Company> getAll() {
        return companyService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        return companyService.create(company);
    }

    @GetMapping("/{companyId}")
    public Optional<Company> searchById(@PathVariable("companyId") Integer companyId) {
        return companyService.searchById(companyId);
    }

    @GetMapping("/{companyId}/employees")
    public List<Employee> getEmployeesByCompanyId(@PathVariable("companyId") Integer companyId) {
        return companyService.getEmployeesByCompanyId(companyId);
    }

    @PutMapping("/{companyId}")
    public Company update(@PathVariable("companyId") Integer companyId, @RequestBody Company updatedCompany) {
        return companyService.update(companyId, updatedCompany);
    }

    @DeleteMapping("/{companyId}")
    public void delete(@PathVariable("companyId") Integer companyId) {
        companyService.delete(companyId);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getCompaniesByPageAndPageSize(@RequestParam("page") Integer page,
                                                       @RequestParam("pageSize") Integer pageSize) {
        return companyService.getCompaniesByPageAndPageSize(page, pageSize);
    }
}
