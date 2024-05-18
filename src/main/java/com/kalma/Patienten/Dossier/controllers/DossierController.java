package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.dto.DossierDto;
import jakarta.validation.Valid;
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

    //todo figure out if this is ever needed, probably not, maybe for an ADMIN.
    @PostMapping
    public ResponseEntity<Object> createDossier(@Valid @RequestBody DossierDto dossierDto, BindingResult br) {
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
            Long placeHolderPatientId = null;
            dossierDto = dossierService.createDossier(placeHolderPatientId, dossierDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/" + dossierDto.id).
                            toUriString());

            return ResponseEntity.created(uri).body(dossierDto);
        }
    }

    @PutMapping("/close_or_open")
    public ResponseEntity<Object> closeOrOpenDossier(@RequestParam Long dossierId,
                                                     @RequestHeader("Authorization") String token
    ) {
        boolean newValueDossierIsClosed = dossierService.closeOrOpenDossier(dossierId, token);

        return ResponseEntity.ok().body("dossier_is_closed for dossier with id " + dossierId + " has been set to " + newValueDossierIsClosed);
    }
}
