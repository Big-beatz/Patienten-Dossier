package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public List<Employee> findEmployeeById(Long id);
    public Optional<Employee> findByUsername(String username);

}

