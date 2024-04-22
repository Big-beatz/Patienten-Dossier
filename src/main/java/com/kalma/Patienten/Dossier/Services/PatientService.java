package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repos;
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository repos, PatientRepository patientRepository) {
    this.repos = repos;
        this.patientRepository = patientRepository;
    }

    public PatientDto createPatient(PatientDto patientDto){
        Patient patient = new Patient();
        patient.setFirstName(patientDto.firstName);
        patient.setLastName(patientDto.lastName);
        patient.setFullName(patientDto.fullName);
        repos.save(patient);
        patientDto.id = patient.getId();

        return patientDto;
    }

    public List<PatientDto> getAllPatients(){
        List<Patient> patients = repos.findAll();
        List<PatientDto> patientDtos = new ArrayList<>();

        for (Patient p : patients) {
            patientDtos.add(patientToDto(p));
        }
        return patientDtos;
    }

    public Patient dtoToPatient(PatientDto patientDto){
        Patient patient = new Patient();

        patient.setFirstName(patientDto.firstName);
        patient.setLastName(patientDto.lastName);
        patient.setFullName(patientDto.fullName);

        return patient;
    }

    public PatientDto patientToDto(Patient patient){
        PatientDto patientDto = new PatientDto();

        patientDto.id = patient.getId();
        patientDto.firstName = patient.getFirstName();
        patientDto.lastName = patient.getLastName();
        patientDto.fullName = patient.getFullName();

        return patientDto;
    }
}
