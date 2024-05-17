package com.kalma.Patienten.Dossier.Services;


import com.kalma.Patienten.Dossier.controllers.ExceptionController;
import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.exceptions.InputNotValidException;
import com.kalma.Patienten.Dossier.exceptions.UsernameAlreadyExistsException;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.models.Role;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;

import com.kalma.Patienten.Dossier.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExceptionController exceptionController;

    public EmployeeService(EmployeeRepository repository,
                           PatientRepository patientRepository,
                           PatientService patientService,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, ExceptionController exceptionController) {
        this.employeeRepository = repository;
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.exceptionController = exceptionController;
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = dtoToEmployee(employeeDto);

        //check if role is valid
        boolean allRolesExist = employeeDto.roles.stream()
                .allMatch(roleRepository::existsByRolename);
        Set<Role> employeeRoles = employee.getRoles();

        if (allRolesExist) {
            for (String rolename : employeeDto.roles) {
                Optional<Role> optionalRole = roleRepository.findById(rolename);
                optionalRole.ifPresent(employeeRoles::add);
            }
        } else {
            throwInputNotValidException("Role doesn't exist, valid inputs are: ROLE_USER, ROLE_ADMIN");
        }

        checkIfUserNameExists(employeeDto.firstName + "." + employeeDto.lastName);

        employeeRepository.save(employee);

        employeeDto.id = employee.getId();
        employeeDto.username = employee.getUsername();


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

        employee.setId(dto.id);
        employee.setFirstName(dto.firstName);
        employee.setLastName(dto.lastName);
        employee.setUsername(dto.firstName + "." + dto.lastName);
        employee.setFunction(dto.function);
        employee.setPassword(passwordEncoder.encode(dto.password));

        return employee;
    }

    public void throwInputNotValidException(String message) throws InputNotValidException {
        throw new InputNotValidException(message);
    }

    public void checkIfUserNameExists(String username) throws UsernameAlreadyExistsException {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUsername(username);
        if (optionalEmployee.isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + username + " already exists");
        }
    }

    public EmployeeDto employeeToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.id = employee.getId();
        employeeDto.firstName = employee.getFirstName();
        employeeDto.lastName = employee.getLastName();
        employeeDto.username = employee.getUsername();
        employeeDto.function = employee.getFunction();
        employeeDto.password = employee.getPassword();

        return employeeDto;
    }
}
