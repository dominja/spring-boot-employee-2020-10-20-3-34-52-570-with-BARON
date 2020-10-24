package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

    private CompanyService companyService;
    private CompanyMapper companyMapper;
    private EmployeeMapper employeeMapper;

    public CompaniesController(CompanyService companyService, CompanyMapper companyMapper, EmployeeMapper employeeMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public List<CompanyResponse> getAll() {
        return companyService.getAll()
                .stream().map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@RequestBody CompanyRequest companyRequest) {
        Company company = companyMapper.toEntity(companyRequest);
        return companyMapper.toResponse(companyService.create(company));
    }

    @GetMapping("/{companyId}")
    public CompanyResponse searchById(@PathVariable("companyId") Integer companyId) {
        Company company = companyService.searchById(companyId);
        return companyMapper.toResponse(company);
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
