package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierRepository extends JpaRepository<Employee, Long> {

}
