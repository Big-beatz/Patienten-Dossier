package com.kalma.Patienten.Dossier.Services;


import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;

    public EmployeeService(EmployeeRepository repository, PatientRepository patientRepository, PatientService patientService) {
        this.employeeRepository = repository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = dtoToEmployee(employeeDto);
        employeeRepository.save(employee);

        employeeDto.id = employee.getId();
        employeeDto.fullName = employee.getFullName();
        for (Long id : employeeDto.patientIds){
            Optional<Patient> optionalPatient = patientRepository.findById(id);
            if (optionalPatient.isPresent()) {
                Patient patient = optionalPatient.get();
                employee.getPatients().add(patient);
            }
            employeeRepository.save(employee);
        }

        return employeeDto;
    }

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDtos.add(employeeToDto(employee));
        }

        return employeeDtos;
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        EmployeeDto employeeDto = employeeToDto(employee);

        return employeeDto;
    }

    public List<Long> getPatientIdList(Employee employee) {
        List<Long> patientsIdList = new ArrayList();
        log.info(employee.getPatients().toString());
        for(Patient patient : employee.getPatients()){
            patientsIdList.add(patient.getId());
        }
        return patientsIdList;
    }


    //mapping functions
    public Employee dtoToEmployee(EmployeeDto dto) {
        Employee employee = new Employee();

        employee.setFirstName(dto.firstName);
        employee.setLastName(dto.lastName);
        employee.setFullName(dto.fullName);
        employee.setRole(dto.role);
        return employee;
    }


    public EmployeeDto employeeToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.id = employee.getId();
        employeeDto.firstName = employee.getFirstName();
        employeeDto.lastName = employee.getLastName();
        employeeDto.fullName = employeeDto.firstName + " " + employeeDto.lastName;
        employeeDto.role = employee.getRole();

        employeeDto.patientIds = getPatientIdList(employee);

        return employeeDto;
    }
}
