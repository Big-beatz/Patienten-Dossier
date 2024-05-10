package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.Services.PatientService;
import com.kalma.Patienten.Dossier.dto.PatientDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {


    private final PatientService patientService;
    private final DossierService dossierService;

    public PatientController(PatientService patientService, DossierService dossierService) {
        this.patientService = patientService;
        this.dossierService = dossierService;
    };

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
}
