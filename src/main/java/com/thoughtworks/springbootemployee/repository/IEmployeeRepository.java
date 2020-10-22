package com.thoughtworks.springbootemployee.repositories;

import com.thoughtworks.springbootemployee.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByGender(String gender);
}
