package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.models.Role;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.ReportRepository;
import com.kalma.Patienten.Dossier.repository.RoleRepository;
import com.kalma.Patienten.Dossier.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {


    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    ReportRepository reportRepository;
    @Mock
    JwtService jwtService;
    @Mock
    ExceptionService exceptionService;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    EmployeeService employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Role role;
    private Report report;
    private List<Report> reports;

    private final String token = "Bearer testToken";
    private final String doctor = "doctor";
    private final String secretary = "secretary";


    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Johnny");
        employee.setLastName("Test");
        employee.setPassword(passwordEncoder.encode("password"));

        employeeDto = new EmployeeDto();
        employeeDto.id = employee.getId();
        employeeDto.firstName = employee.getFirstName();
        employeeDto.lastName = employee.getLastName();
        employeeDto.username = "Johnny.Test";
        employeeDto.function = doctor;
        employeeDto.password = "password";
        employeeDto.roles = List.of("ROLE_USER");

        role = new Role();
        role.setRolename("ROLE_USER");

        report = new Report();
        report.setEmployee(employee);
        report.setBody("beetje testdata");
        report.setId(1L);

        reports = new ArrayList<>();
        reports.add(report);
    }

    @Test
    public void shouldCreateEmployee() {
        when(roleRepository.existsByRolename(anyString())).thenReturn(true);
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee savedEmployee = invocation.getArgument(0);
            savedEmployee.setId(1L);
            return savedEmployee;
        });        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(exceptionService, never()).InputNotValidException(anyString());

        assertEquals(1L, result.id);
        assertEquals("Johnny.Test", result.username);
        assertEquals(passwordEncoder.encode("password"), result.password);
    }

    @Test
    void createEmployeeWithInvalidRole() {
        when(roleRepository.existsByRolename(anyString())).thenReturn(false);

        doThrow(new RuntimeException("Role doesn't exist, valid values are: ROLE_USER, ROLE_ADMIN"))
                .when(exceptionService).InputNotValidException(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(employeeDto));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(exceptionService, times(1)).InputNotValidException(anyString());
    }

    @Test
    void createEmployeeWithInvalidFunction() {
        employeeDto.function = "invalid_function";

        doThrow(new RuntimeException("Function is not accepted, valid values are: doctor, secretary"))
                .when(exceptionService).InputNotValidException(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(employeeDto));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(exceptionService, times(1)).InputNotValidException(anyString());
    }

    @Test
    void createEmployeeWithExistingUsername() {
        when(roleRepository.existsByRolename(anyString())).thenReturn(true);
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.of(employee));

        doThrow(new RuntimeException("Username already exists")).when(exceptionService).UsernameAlreadyExistsException(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(employeeDto));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void stReportsToNUllWhenDeletingEmployee() {
        Employee employeeToDelete = new Employee();
        employeeToDelete.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeToDelete));

        reports = Collections.singletonList(report);
        when(reportRepository.findByEmployeeId(1L)).thenReturn(reports);

        String result = employeeService.deleteEmployee(1L);

        for (Report report : reports) {
            verify(reportRepository).save(report);
        }

        verify(employeeRepository).delete(employeeToDelete);

        assertNull(report.getEmployee());

        assertEquals("Employee deleted successfully", result);
    }

    @Test
    void deleteEmployeeLastAdmin() {
        Role adminRole = new Role();
        adminRole.setRolename("ROLE_ADMIN");

        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setFirstName("Admin");
        adminEmployee.setLastName("User");
        adminEmployee.setRoles(Collections.singleton(adminRole));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(adminEmployee));

        doThrow(new RuntimeException("It is not allowed to delete the last employee with the role of Admin"))
                .when(exceptionService).DeleteNotAllowed(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(1L));

        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void deleteEmployeeNotAdmin() {
        Role userRole = new Role();
        userRole.setRolename("ROLE_USER");

        Employee employeeToDelete = new Employee();
        employeeToDelete.setId(2L);
        employeeToDelete.setFirstName("Lola");
        employeeToDelete.setLastName("Bunny");
        employeeToDelete.setRoles(Collections.singleton(userRole));

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employeeToDelete));
        when(employeeRepository.findAll()).thenReturn(List.of(employee, employeeToDelete));

        String result = employeeService.deleteEmployee(2L);

        verify(employeeRepository, times(1)).delete(any(Employee.class));

        assertEquals("Employee deleted successfully", result);
    }

    @Test
    void getEmployeeByTokenSuccess() {
        when(jwtService.extractUsername(anyString())).thenReturn(employeeDto.username);
        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeByToken(token);

        verify(jwtService, times(1)).extractUsername(anyString());
        verify(employeeRepository, times(1)).findEmployeeByUsername(anyString());

        assertEquals(employee, result);
    }

    @Test
    void getEmployeeByTokenNotFound() {
        when(jwtService.extractUsername(anyString())).thenReturn(employeeDto.username);
        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.empty());

        doThrow(new RuntimeException("Employee " + employeeDto.username + " is not found"))
                .when(exceptionService).RecordNotFoundException(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeByToken(token));

        verify(jwtService, times(1)).extractUsername(anyString());
        verify(employeeRepository, times(1)).findEmployeeByUsername(anyString());
    }

    @Test
    void getEmployeeByIdSuccess() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(employee.getId());

        verify(employeeRepository, times(1)).findById(employee.getId());

        assertEquals(employee, result);
    }

    @Test
    void getEmployeeByIdNotFound() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        doThrow(new RuntimeException("Employee " + employee.getId() + " is not found"))
                .when(exceptionService).RecordNotFoundException(anyString());

        assertThrows(RuntimeException.class, () -> employeeService.getEmployeeById(employee.getId()));

        verify(employeeRepository, times(1)).findById(employee.getId());
    }
    @Test
    void checkIfUserIsSecretary() {
        employee.setFunction("doctor");

        employeeService.checkIfUserIsSecretary(employee);

        verify(exceptionService, times(1)).InvalidFunctionException("User is not a secretary");
    }


    @Test
    void checkIfUserIsSecretaryHappyFlow() {
        employee.setFunction("secretary");

        employeeService.checkIfUserIsSecretary(employee);

        verify(exceptionService, never()).InvalidFunctionException(anyString());
    }

    @Test
    void checkIfEmployeeExistsHappyFlow() {
        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.of(employee));

        employeeService.checkIfEmployeeExists(employee.getUsername());

        verify(exceptionService, never()).RecordNotFoundException(anyString());
    }

    @Test
    void checkIfUserExists() {
        when(employeeRepository.findEmployeeByUsername(anyString())).thenReturn(Optional.empty());

        employeeService.checkIfEmployeeExists(employee.getUsername());

        verify(exceptionService, times(1)).RecordNotFoundException("Employee not found");
    }



    @Test
    void getAllEmployees() {
        List<Employee> mockEmployees = new ArrayList<>();

        Employee employeeLola = new Employee();
        employeeLola.setId(2L);
        employeeLola.setFirstName("Lola");
        employeeLola.setLastName("Bunny");
        employeeLola.setFunction("secretary");

        mockEmployees.add(employee);
        mockEmployees.add(employeeLola);

        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<EmployeeDto> employeeDtos = employeeService.getAllEmployees();

        assertEquals(mockEmployees.size(), employeeDtos.size());
        assertEquals(employee.getId(), employeeDtos.get(0).id);
        assertEquals(employee.getFirstName(), employeeDtos.get(0).firstName);
        assertEquals(employee.getLastName(), employeeDtos.get(0).lastName);
        assertEquals(employee.getFunction(), employeeDtos.get(0).function);

        assertEquals(employeeLola.getId(), employeeDtos.get(1).id);
        assertEquals(employeeLola.getFirstName(), employeeDtos.get(1).firstName);
        assertEquals(employeeLola.getLastName(), employeeDtos.get(1).lastName);
        assertEquals(employeeLola.getFunction(), employeeDtos.get(1).function);
    }




}