package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.repository.DossierRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;

public class DossierService {
    private final DossierRepository dossierRepository;

    public DossierService(DossierRepository repository) {
        this.dossierRepository = repository;
    }


}
