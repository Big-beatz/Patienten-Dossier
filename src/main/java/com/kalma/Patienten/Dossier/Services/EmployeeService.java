package com.kalma.Patienten.Dossier.Services;


import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.models.Role;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;

import com.kalma.Patienten.Dossier.repository.ReportRepository;
import com.kalma.Patienten.Dossier.repository.RoleRepository;
import com.kalma.Patienten.Dossier.security.JwtService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExceptionService exceptionService;
    private final JwtService jwtService;
    private final ReportRepository reportRepository;

    public EmployeeService(EmployeeRepository repository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           ExceptionService exceptionService,
                           JwtService jwtService,
                           ReportRepository reportRepository
    ) {
        this.employeeRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.exceptionService = exceptionService;
        this.jwtService = jwtService;
        this.reportRepository = reportRepository;
    }

    public String doctor = "doctor";
    private String secretary = "secretary";

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        //check if function is allowed
        if (employeeDto.function.equalsIgnoreCase(doctor) || employeeDto.function.equalsIgnoreCase(secretary)) {
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
                exceptionService.InputNotValidException("Role doesn't exist, valid values are: ROLE_USER, ROLE_ADMIN");
            }

            checkIfUserNameExists(employeeDto.firstName + "." + employeeDto.lastName);

            employeeRepository.save(employee);

            employeeDto.id = employee.getId();
            employeeDto.username = employee.getUsername();
            employeeDto.password = employee.getPassword();

        }
        else{
            exceptionService.InputNotValidException("Function is not accepted, valid values are: " + doctor + ", " + secretary);
        }
        return employeeDto;
    }

    public String deleteEmployee(Long employeeId) {
        //check if employeeToDelete is not the last admin
        Employee employeeToDelete = getEmployeeById(employeeId);

        long adminCount = employeeRepository.findAll().stream()
                .flatMap(employee -> employee.getRoles().stream())
                .filter(role -> role.getRolename().equalsIgnoreCase("ROLE_ADMIN"))
                .count();

        boolean isDeletingLastAdmin = adminCount == 1 && employeeToDelete.getRoles().stream()
                .anyMatch(role -> role.getRolename().equalsIgnoreCase("ROLE_ADMIN"));

        if(isDeletingLastAdmin) {
            exceptionService.DeleteNotAllowed("It is not allowed to delete the last employee with the role of Admin");
        }

        //done like this to prevent circular dependency
        List<Report> reports = reportRepository.findByEmployeeId(employeeId);
        for (Report report : reports) {
            report.setEmployee(null);
            reportRepository.save(report);
        }


        employeeRepository.delete(employeeToDelete);


        return "Employee deleted successfully";
    }

    public void checkIfUserNameExists(String username) {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUsername(username);
        if (optionalEmployee.isPresent()) {
            exceptionService.UsernameAlreadyExistsException(username);
        }
    }

    public void checkIfUserIsSecretary(Employee employee) {
            if(!employee.getFunction().equalsIgnoreCase(secretary)) {
                exceptionService.InvalidFunctionException("User is not a " + secretary);
            }
    }

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for (Employee employee : employees) {
            employeeDtos.add(employeeToDto(employee));
        }

        return employeeDtos;
    }

    public Employee getEmployeeByToken(String token){
        Employee employee = new Employee();

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        //get username
        String username = jwtService.extractUsername(token);

        //find employee
        Optional<Employee> optionalEmployee= employeeRepository.findEmployeeByUsername(username);
        if(optionalEmployee.isPresent()) {
            employee = optionalEmployee.get();
        } else{
            exceptionService.RecordNotFoundException("Employee " + username + " is not found");
        }
        return employee;
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee employee = new Employee();

        if(optionalEmployee.isPresent()) {
            employee = optionalEmployee.get();
        } else{
            exceptionService.RecordNotFoundException("Employee " + id + "is not found");
        }
        return employee;
    }

    public void checkIfEmployeeExists(String username) {
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeeByUsername(username);
        if (optionalEmployee.isEmpty()) {
            exceptionService.RecordNotFoundException("Employee not found");
        }
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
