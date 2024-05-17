package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.controllers.DossierController;
import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.DossierRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final DossierService dossierService;

    public PatientService(PatientRepository repository, DossierService dossierService, DossierController dossierController, DossierRepository dossierRepository) {
    this.patientRepository = repository;
    this.dossierService = dossierService;
    }

    //todo can only be done by Secretary
    public PatientDto createPatient(PatientDto patientDto){
        Patient patient = dtoToPatient(patientDto);

        //create dossierDto
        DossierDto dossierDto = new DossierDto();
        dossierDto.name = patientDto.fullName;
        dossierDto.dossierIsClosed = false;
        dossierService.createDossier(patient.getId(), dossierDto);

        patient.setDossier(dossierService.getDossierByName(dossierDto.name));
        patientDto.dossierId = patient.getDossier().getId();

        patientRepository.save(patient);

        patientDto.id = patient.getId();
        patientDto.fullName=patient.getFullName();

        return patientDto;
    }

    //todo can only be done by Secretary
    public List<PatientDto> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        List<PatientDto> patientDtos = new ArrayList<>();

        for (Patient patient : patients) {
            patientDtos.add(patientToDto(patient));
        }
        return patientDtos;
    }

    public List<Long> getEmployeeIdList(Patient patient) {
        List<Long> employeeIdList = new ArrayList();
        for(Employee employee : patient.getEmployees()){
            employeeIdList.add(employee.getId());
        }
        return employeeIdList;
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findPatientById(id);
    }

    public LocalDate setNextAppointMent(Patient patient) {
        return patient.getNextAppointment();
    }

    //mapping functions
    public Patient dtoToPatient(PatientDto patientDto){
        Patient patient = new Patient();

        patient.setFirstName(patientDto.firstName);
        patient.setLastName(patientDto.lastName);
        patient.setFullName(patientDto.firstName + " " + patientDto.lastName);
//
        return patient;
    }

    public PatientDto patientToDto(Patient patient){
        PatientDto patientDto = new PatientDto();

        patientDto.id = patient.getId();
        patientDto.firstName = patient.getFirstName();
        patientDto.lastName = patient.getLastName();
        patientDto.fullName = patientDto.firstName + " " + patientDto.lastName;
        patientDto.nextAppointment = patient.getNextAppointment();

        if(patient.getDossier() != null) {
            patientDto.dossierId = patient.getDossier().getId();
        }
        if(patient.getEmployees() != null) {
            patientDto.employeeIds = getEmployeeIdList(patient);
        }

        return patientDto;
    }
}
