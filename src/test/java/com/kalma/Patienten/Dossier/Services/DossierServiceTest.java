package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.repository.DossierRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class DossierServiceTest {

    @Mock
    DossierRepository dossierRepository;
    @Mock
    PatientRepository patientRepository;
    @Mock
    ExceptionService exceptionService;
    @Mock
    EmployeeService employeeService;
    @InjectMocks
    DossierService dossierService;

    private DossierDto dossierDto;
    private Dossier dossier;
    private List<Report> reports;
    private Report report;
    private Patient patient;
    private String token = "This is a token";
    private Employee employee;


    @BeforeEach
        void setUp() {
        report = new Report();
        report.setBody("beetje testdata");
        report.setId(1L);

        reports = new ArrayList<>();
        reports.add(report);

        dossier = new Dossier();
        dossier.setId(1L);
        dossier.setName("Bugs Bunny");
        dossier.setDossierIsClosed(false);
        dossier.setReports(reports);

        dossierDto = new DossierDto();
        dossierDto.name = "Bugs Bunny";
        dossierDto.dossierIsClosed = false;
        dossierDto.id = 1L;

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Daffy");
        patient.setLastName("Duck");
        patient.setFullName("Daffy Duck");

        employee = new Employee();
        employee.setId(1L);
        employee.setFunction("secretary");

    }

    @Test
    void shouldCreateDossier() {
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(dossierRepository.save(ArgumentMatchers.any(Dossier.class))).thenAnswer(invocation -> {
            Dossier savedDossier = invocation.getArgument(0);
            savedDossier.setId(1L);
            return savedDossier;
        });
        when(patientRepository.save(ArgumentMatchers.any(Patient.class))).thenAnswer(invocation -> {
            Patient savedPatient = invocation.getArgument(0);
            savedPatient.setId(1L);
            return savedPatient;
        });

        // Act
        DossierDto result = dossierService.createDossier(patientId, dossierDto);

        // Assert
        assertNotNull(result);
        assertEquals(dossier.getId(), result.id);
        assertEquals(dossier.getDossierIsClosed(), result.dossierIsClosed);
        assertEquals(patient.getFullName(), result.name);

        ArgumentCaptor<Dossier> dossierCaptor = forClass(Dossier.class);
        verify(dossierRepository, times(1)).save(dossierCaptor.capture());
        assertEquals(dossier.getId(), dossierCaptor.getValue().getId());

        ArgumentCaptor<Patient> patientCaptor = forClass(Patient.class);
        verify(patientRepository, times(1)).save(patientCaptor.capture());
        assertEquals(patient.getId(), patientCaptor.getValue().getId());

        verify(patientRepository, times(1)).findById(patientId);
        verify(exceptionService, never()).RecordNotFoundException(anyString());
    }

    @Test
    void createShouldThrowRecordNotFoundException() {
        Long patientId = 1L;
        patient.setDossier(dossier);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        doThrow(new RuntimeException("patient not found"))
                .when(exceptionService).RecordNotFoundException(anyString());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dossierService.createDossier(patientId, dossierDto));


        verify(patientRepository, times(1)).findById(patientId);
        verify(exceptionService, times(1)).RecordNotFoundException("patient not found");
        verify(dossierRepository, never()).save(dossier);
        verify(patientRepository, never()).save(patient);
    }

    @Test
    void shouldCreateDossierWithoutLinking(){
        when(dossierRepository.save(ArgumentMatchers.any(Dossier.class))).thenAnswer(invocation -> {
            Dossier savedDossier = invocation.getArgument(0);
            savedDossier.setId(1L);
            return savedDossier;
        });
        // Act
        DossierDto result = dossierService.createDossier(null, dossierDto);

        // Assert
        assertNotNull(result);
        assertEquals(dossier.getId(), result.id);
        assertEquals(dossier.getDossierIsClosed(), result.dossierIsClosed);
        verify(patientRepository, never()).findById(anyLong());
        ArgumentCaptor<Dossier> dossierCaptor = forClass(Dossier.class);
        verify(dossierRepository, times(1)).save(dossierCaptor.capture());
        assertEquals(dossier.getId(), dossierCaptor.getValue().getId());

        ArgumentCaptor<Patient> patientCaptor = forClass(Patient.class);
        verify(patientRepository, never()).save(patientCaptor.capture());
        verify(exceptionService, never()).RecordNotFoundException(anyString());
    }

    @Test
    void shouldGetAllDossiers() {
        List<Dossier> mockDossiers = new ArrayList<>();

        Dossier dossierLola = new Dossier();
        dossierLola.setId(2L);
        dossierLola.setName("Lola");
        dossierLola.setReports(reports);
        dossierLola.setDossierIsClosed(true);

        mockDossiers.add(dossier);
        mockDossiers.add(dossierLola);

        when(dossierRepository.findAll()).thenReturn(mockDossiers);

        List<DossierDto> dossierDtos = dossierService.getAllDossiers();

        assertEquals(mockDossiers.size(), dossierDtos.size());
        assertEquals(dossier.getId(), dossierDtos.get(0).id);
        assertEquals(dossier.getName(), dossierDtos.get(0).name);
        assertEquals(dossier.getDossierIsClosed(), dossierDtos.get(0).dossierIsClosed);

        assertEquals(dossierLola.getId(), dossierDtos.get(1).id);
        assertEquals(dossierLola.getName(), dossierDtos.get(1).name);
        assertEquals(dossierLola.getDossierIsClosed(), dossierDtos.get(1).dossierIsClosed);
    }

    @Test
    void shouldReturnDossierWhenFoundByName() {
        when(dossierRepository.findByName(dossier.getName())).thenReturn(Optional.of(dossier));

        Dossier result = dossierService.getDossierByName(dossier.getName());

        assertNotNull(result);
        assertEquals(dossier.getId(), result.getId());
        assertEquals(dossier.getName(), result.getName());
        assertEquals(dossier.getDossierIsClosed(), result.getDossierIsClosed());

        verify(dossierRepository, times(1)).findByName(dossier.getName());
        verify(exceptionService, never()).RecordNotFoundException(anyString());
    }

    @Test
    void shouldThrowExceptionWhenDossierNotFoundByName() {
        String dossierName = "Not existing";

        when(dossierRepository.findByName(dossierName)).thenReturn(Optional.empty());

        doThrow(new RuntimeException("Dossier can not be found")).when(exceptionService).RecordNotFoundException(anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> dossierService.getDossierByName(dossierName));
        assertEquals("Dossier can not be found", exception.getMessage());

        verify(dossierRepository, times(1)).findByName(dossierName);
        verify(exceptionService, times(1)).RecordNotFoundException("Dossier can not be found");
    }

    @Test
    void shouldCloseDossierIfDossierIsOpen(){

        // Mocking the necessary methods
        when(employeeService.getEmployeeByToken(token)).thenReturn(employee);
        doNothing().when(employeeService).checkIfUserIsSecretary(employee);
        when(dossierRepository.findById(dossier.getId())).thenReturn(Optional.of(dossier));
        when(dossierRepository.save(ArgumentMatchers.any(Dossier.class))).thenAnswer(invocation -> {
            Dossier savedDossier = invocation.getArgument(0);
            savedDossier.setId(1L);
            return savedDossier;
        });
        // Act
        boolean result = dossierService.closeOrOpenDossier(dossier.getId(), token);

        // Assert
        assertTrue(result);
        assertTrue(dossier.getDossierIsClosed());
        verify(employeeService, times(1)).getEmployeeByToken(token);
        verify(employeeService, times(1)).checkIfUserIsSecretary(employee);
        verify(dossierRepository, times(1)).findById(dossier.getId());
        verify(dossierRepository, times(1)).save(dossier);
    }

    @Test
    void shouldThrowExceptionWhenDossierNotFound() {
        // Mocking the necessary methods
        when(employeeService.getEmployeeByToken(token)).thenReturn(employee);
        doNothing().when(employeeService).checkIfUserIsSecretary(employee);
        when(dossierRepository.findById(dossier.getId())).thenReturn(Optional.empty());

        // Mock the exceptionService to throw the exception
        doThrow(new RuntimeException("Dossier can not be found")).when(exceptionService).RecordNotFoundException(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> dossierService.closeOrOpenDossier(dossier.getId(), token));
        assertEquals("Dossier can not be found", exception.getMessage());

        verify(employeeService, times(1)).getEmployeeByToken(token);
        verify(employeeService, times(1)).checkIfUserIsSecretary(employee);
        verify(dossierRepository, times(1)).findById(dossier.getId());
        verify(dossierRepository, never()).save(dossier);
        verify(exceptionService, times(1)).RecordNotFoundException("Dossier can not be found");
    }


    @Test
    void shouldReturnDossierWhenFound() {
        // Mocking the necessary methods
        when(dossierRepository.findById(dossier.getId())).thenReturn(Optional.of(dossier));

        // Act
        Dossier result = dossierService.getDossierById(dossier.getId());

        // Assert
        assertNotNull(result);
        assertEquals(dossier.getId(), result.getId());
        verify(dossierRepository, times(1)).findById(dossier.getId());
        verify(exceptionService, never()).RecordNotFoundException(anyString());
    }

    @Test
    void shouldThrowExceptionWhenDossierNotFoundById() {

        // Mocking the necessary methods
        when(dossierRepository.findById(dossier.getId())).thenReturn(Optional.empty());

        // Mock the exceptionService to throw the exception
        doThrow(new RuntimeException("Dossier can not be found")).when(exceptionService).RecordNotFoundException(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> dossierService.getDossierById(dossier.getId()));
        assertEquals("Dossier can not be found", exception.getMessage());

        verify(dossierRepository, times(1)).findById(dossier.getId());
        verify(exceptionService, times(1)).RecordNotFoundException("Dossier can not be found");
    }
}