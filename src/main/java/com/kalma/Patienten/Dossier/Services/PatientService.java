package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;

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
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final ExceptionService exceptionService;

    public PatientService(PatientRepository repository,
                          DossierService dossierService,
                          EmployeeRepository employeeRepository,
                          EmployeeService employeeService,
                          ExceptionService exceptionService
    ) {
        this.patientRepository = repository;
        this.dossierService = dossierService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.exceptionService = exceptionService;
    }

    public PatientDto createPatient(PatientDto patientDto, String token){
        //check if user is allowed to create patient
        Employee employee = employeeService.getEmployeeByToken(token);
        employeeService.checkIfUserIsSecretary(employee);

        Patient patient = dtoToPatient(patientDto);

        //create dossierDto
        DossierDto dossierDto = new DossierDto();
        dossierDto.name = patientDto.firstName + " " + patientDto.lastName; ;
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
        //getPatient
        Patient patient = getPatientById(patientId);

        //checkIfDossierIsClossed
        if(patient.getDossier().getDossierIsClosed()) {
            exceptionService.AddingReportNotAllowedException("Dossier of "+ patient.getFullName() + " is closed");
        }

        // set appointment
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
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        Patient patient = new Patient();

        if(optionalPatient.isPresent()) {
            patient = optionalPatient.get();
        } else{
            exceptionService.RecordNotFoundException("Employee " + id + "is not found");
        }
        return patient;
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
