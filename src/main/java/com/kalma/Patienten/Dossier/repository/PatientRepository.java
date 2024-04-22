package com.kalma.Patienten.Dossier.repository;

import com.kalma.Patienten.Dossier.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    public List<Patient> findPatientByFirstName(String firstName);

    public List<Patient> findPatientByLastNameContaining(String lastName);

}
