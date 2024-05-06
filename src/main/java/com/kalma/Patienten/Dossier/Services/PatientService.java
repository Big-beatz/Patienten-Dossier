package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository repository) {
    this.patientRepository = repository;
    }

    public PatientDto createPatient(PatientDto patientDto){
        Patient patient = dtoToPatient(patientDto);
        patientRepository.save(patient);

        patientDto.id = patient.getId();
        patientDto.fullName=patient.getFullName();

        return patientDto;
    }

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


    //mapping functions
    public Patient dtoToPatient(PatientDto dto){
        Patient patient = new Patient();

        patient.setFirstName(dto.firstName);
        patient.setLastName(dto.lastName);
        patient.setFullName(dto.fullName);

        return patient;
    }

    public PatientDto patientToDto(Patient patient){
        PatientDto patientDto = new PatientDto();

        patientDto.id = patient.getId();
        patientDto.firstName = patient.getFirstName();
        patientDto.lastName = patient.getLastName();
        patientDto.fullName = patientDto.firstName + " " + patientDto.lastName;
        patientDto.dossierId = patient.getDossier().getId();
        patientDto.employeeIds = getEmployeeIdList(patient);

        return patientDto;
    }
}
