package com.thoughtworks.springbootemployee.repository;

import com.thoughtworks.springbootemployee.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {
}
