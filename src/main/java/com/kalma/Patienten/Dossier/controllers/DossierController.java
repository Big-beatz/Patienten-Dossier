package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.Services.ReportService;
import com.kalma.Patienten.Dossier.dto.DossierDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dossiers")
public class DossierController {

    private final DossierService dossierService;

    public DossierController(DossierService dossierService) {
        this.dossierService = dossierService;
    }

    @GetMapping
    public ResponseEntity<List<DossierDto>> getAllDossiers() {
        return ResponseEntity.ok(dossierService.getAllDossiers());
    }

//    @PostMapping
//    public ResponseEntity<Object> createDossier(@RequestBody DossierDto dossierDto, BindingResult br) {
//        if (br.hasFieldErrors()) {
//            StringBuilder sb = new StringBuilder();
//            for (FieldError fieldError : br.getFieldErrors()) {
//                sb.append(fieldError.getField());
//                sb.append(": ");
//                sb.append(fieldError.getDefaultMessage());
//                sb.append("\n");
//            }
//            return ResponseEntity.badRequest().body(sb.toString());
//        } else {
//            dossierDto = dossierService.createDossier(dossierDto);
//
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder.
//                            fromCurrentRequest().
//                            path("/" + dossierDto.id).
//                            toUriString());
//
//            return ResponseEntity.created(uri).body(dossierDto);
//        }
//    }
}
