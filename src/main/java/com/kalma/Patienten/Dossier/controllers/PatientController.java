package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.Services.PatientService;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/patients")
public class PatientController {


    private final PatientService patientService;
    private final DossierService dossierService;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;

    public PatientController(PatientService patientService, DossierService dossierService, PatientRepository patientRepository, EmployeeRepository employeeRepository) {
        this.patientService = patientService;
        this.dossierService = dossierService;
        this.patientRepository = patientRepository;
        this.employeeRepository = employeeRepository;
    }

    ;

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
//    can move these to service
//    @GetMapping("/firstName")
//    public ResponseEntity<List<Patient>> getPatientByFirstName(@RequestParam String firstName) {
//        return ResponseEntity.ok(patientRepository.findPatientByFirstName(firstName));
//    }
//
//    @GetMapping("/lastName")
//    public ResponseEntity<List<Patient>> getPatientByLastName(@RequestParam String lastName) {
//        return ResponseEntity.ok(patientRepository.findPatientByLastNameContaining(lastName));
//    }
//
//    @GetMapping("/partialName")
//    public ResponseEntity<List<Patient>> getPatientBySubString(@RequestParam String partialName) {
//        return ResponseEntity.ok(patientRepository.findPatientByFullNameContaining(partialName));
//    }

    @PostMapping
    public ResponseEntity<Object> createPatient(@Valid @RequestBody PatientDto patientDto, BindingResult br) {

        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : br.getFieldErrors()) {
                sb.append(fieldError.getField());
                sb.append(": ");
                sb.append(fieldError.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            patientDto = patientService.createPatient(patientDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/" + patientDto.id).
                            toUriString());

            return ResponseEntity.created(uri).body(patientDto);
        }
    }

    @PutMapping("/appointment")
    public ResponseEntity<Object> putNextAppointment(
            @Valid
            @RequestParam("nextAppointment")String nextAppointmentString,
            @RequestParam("patientId") Long patientId,
            @RequestParam("employeeUsername") String employeeUsername) {
        LocalDate nextAppointment;
        try {
            nextAppointment = LocalDate.parse(nextAppointmentString);
        } catch (DateTimeParseException exception) {
            return ResponseEntity.badRequest().
                    body("Invalid date format for nextAppointment. Expected format: YYYY-MM-DD");
        }

        // Additional validation logic can be added here
        if (patientId == null || patientId <= 0) {
            return ResponseEntity.badRequest().body("Invalid patientId");
        }

        if (employeeUsername == null || employeeUsername.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid employeeUsername");
        }
        else {
            //set appointment
            Patient patient = patientRepository.findPatientById(patientId);
            patient.setNextAppointment(nextAppointment);

            //setEmployee
            Optional<Employee> optionalEmployee = employeeRepository.findByUsername(employeeUsername);
            if (!optionalEmployee.isPresent()) {
                return ResponseEntity.badRequest().body("Employee not found");
            }

            Employee employee = optionalEmployee.get();
            Set<Employee> employees = patient.getEmployees();

            //check if link between patient and employee exists if not make the link
            boolean employeeAndPatientLinkExists = employees.stream().anyMatch(e -> e.getId().equals(employee.getId()));
            if(!employeeAndPatientLinkExists) {
                employees.add(employee);
                patient.setEmployees(employees);
            }

            patientRepository.save(patient);

            return ResponseEntity.ok("Next Appointment on " + nextAppointmentString + " with " + employeeUsername);
        }
    }
}
