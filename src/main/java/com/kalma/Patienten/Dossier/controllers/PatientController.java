package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.Services.ExceptionService;
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
    private final ExceptionService exceptionService;

    public PatientController(PatientService patientService,
                             ExceptionService exceptionService) {
        this.patientService = patientService;
        this.exceptionService = exceptionService;
    }


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
    public ResponseEntity<Object> createPatient(@Valid @RequestBody PatientDto patientDto, @RequestHeader("Authorization") String token, BindingResult br) {

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
            patientDto = patientService.createPatient(patientDto, token);

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
            @RequestParam("nextAppointment") String nextAppointmentString,
            @RequestParam("patientId") Long patientId,
            @RequestParam("employeeUsername") String employeeUsername)
    {
        LocalDate nextAppointment = null;

        try {
            nextAppointment = LocalDate.parse(nextAppointmentString);
        } catch (DateTimeParseException exception) {
            exceptionService.InputNotValidException("Date should be formated YYYY-mm-dd");
        }
        if (patientId == null || patientId <= 0) {
            exceptionService.InputNotValidException("Invalid patientId");
        }

        if (employeeUsername == null || employeeUsername.isEmpty()) {
            exceptionService.InputNotValidException("Invalid employeeUsername");
        } else {

            //set appointment
            String appointmentConfirmed = patientService.setNextAppointment(
                    nextAppointmentString,
                    patientId,
                    employeeUsername,
                    nextAppointment
            );
            return ResponseEntity.ok(appointmentConfirmed);
        }
        return null;
    }
}

