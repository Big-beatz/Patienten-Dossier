package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.DossierService;
import com.kalma.Patienten.Dossier.dto.DossierDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dossiers")
public class DossierController {
    
    private final DossierService dossierService;
    
    public DossierController(DossierService dossierService) {
        this.dossierService = dossierService;
    }

    @GetMapping
    public ResponseEntity<List<DossierDto>> getAllDossiers(){
        return ResponseEntity.ok(dossierService.getAllDossiers());
    }

}
