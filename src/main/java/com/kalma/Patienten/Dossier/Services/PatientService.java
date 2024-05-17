package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import com.kalma.Patienten.Dossier.security.JwtService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final DossierService dossierService;
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final ExceptionService exceptionService;


    public PatientService(PatientRepository repository,
                          DossierService dossierService,
                          JwtService jwtService,
                          EmployeeRepository employeeRepository,
                          EmployeeService employeeService,
                          ExceptionService exceptionService
    ) {
        this.patientRepository = repository;
        this.dossierService = dossierService;
        this.jwtService = jwtService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.exceptionService = exceptionService;
    }

    //todo can only be done by Secretary
    public PatientDto createPatient(PatientDto patientDto, String token){
        //check if user is allowed to create patient
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtService.extractUsername(token);
        Optional<Employee> optionalEmployee= employeeRepository.findEmployeeByUsername(username);
        if(optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeService.checkIfUserIsSecretary(employee);
        } else{
            exceptionService.RecordNotFoundException("Username " + username + " is not found");
        }

        Patient patient = dtoToPatient(patientDto);

        //create dossierDto
        DossierDto dossierDto = new DossierDto();
        dossierDto.name = patientDto.fullName;
        dossierDto.dossierIsClosed = false;
        dossierService.createDossier(patient.getId(), dossierDto);

        //setDossier to patient
        patient.setDossier(dossierService.getDossierByName(dossierDto.name));
        patientDto.dossierId = patient.getDossier().getId();

        //setPatientToSecretary
        patientRepository.save(patient);


        patientDto.id = patient.getId();
        patientDto.fullName=patient.getFullName();

        return patientDto;
    }

    public String setNextAppointment(String nextAppointmentString,
                                     Long patientId,
                                     String employeeUsername,
                                     LocalDate nextAppointment
    ){
        Patient patient = patientRepository.findPatientById(patientId);
        patient.setNextAppointment(nextAppointment);

        //check if userName is valid
        employeeService.checkIfEmployeeExists(employeeUsername);

        //setEmployee
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUsername(employeeUsername);
        Employee employee = optionalEmployee.get();

        //checkIfEmployee is doctor
        if(employee.getFunction().equalsIgnoreCase("doctor")){
            Set<Employee> employees = patient.getEmployees();

            //check if link between patient and employee exists if not make the link
            boolean employeeAndPatientLinkExists = employees.stream().anyMatch(e -> e.getId().equals(employee.getId()));
            if(!employeeAndPatientLinkExists) {
                employees.add(employee);
                patient.setEmployees(employees);
            }
        } else {
            exceptionService.InvalidFunctionException(employeeUsername + " is not a " + employeeService.doctor);
        }

        patientRepository.save(patient);

        return ("Next Appointment on " + nextAppointmentString + " with " + employeeUsername);
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
