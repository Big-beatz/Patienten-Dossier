package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.dto.DossierDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/close_or_open")
    public ResponseEntity<Object> closeOrOpenDossier(@RequestParam Long dossierId,
                                                     @RequestHeader("Authorization") String token
    ) {
        boolean newValueDossierIsClosed = dossierService.closeOrOpenDossier(dossierId, token);

        return ResponseEntity.ok().body("dossier_is_closed for dossier with id " + dossierId + " has been set to " + newValueDossierIsClosed);
    }
}
