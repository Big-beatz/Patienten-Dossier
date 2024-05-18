package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByEmployeeId(Long employeeId);
}
