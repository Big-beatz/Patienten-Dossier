package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.repository.DossierRepository;
import com.kalma.Patienten.Dossier.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DossierService {
    private final DossierRepository dossierRepository;
    private final PatientRepository patientRepository;

    public DossierService(DossierRepository repository, PatientRepository patientRepository) {
        this.dossierRepository = repository;
        this.patientRepository = patientRepository;
    }

    public DossierDto createDossier(Long patientId, DossierDto dossierDto) {
        Dossier dossier = dtoToDossier(dossierDto);
        dossierRepository.save(dossier);

        dossierDto.id = dossier.getId();
        dossierDto.dossierIsClosed = dossier.getDossierIsClosed();

        //link to patient
        Patient patientById = patientRepository.findById(patientId).get();
        if (patientById.getDossier() == null) {
            patientById.setDossier(dossier);
            patientRepository.save(patientById);
        }

        return dossierDto;
    }

    public List<DossierDto> getAllDossiers() {
        List<Dossier> dossiers = dossierRepository.findAll();
        List<DossierDto> dossierDtos = new ArrayList<>();

        for (Dossier dossier : dossiers) {
            dossierDtos.add(dossierToDto(dossier));
        }

        return dossierDtos;
    }

    public Optional<Dossier> getDossierById(Long id) {
        return dossierRepository.findById(id);
    }


    //mapping functions
    public DossierDto dossierToDto(Dossier dossier) {
        DossierDto dossierDto = new DossierDto();

        dossierDto.id = dossier.getId();
        dossierDto.name = dossier.getName();
        dossierDto.dossierIsClosed = dossier.getDossierIsClosed();
        dossierDto.reportIds = dossier.getReports();

        return dossierDto;
    }

    public Dossier dtoToDossier(DossierDto dossierDto) {
        Dossier dossier = new Dossier();

        dossier.setId(dossierDto.id);
        dossier.setName(dossierDto.name);
        dossier.setDossierIsClosed(dossierDto.dossierIsClosed);
        dossier.setReports(dossierDto.reportIds);

        return dossier;
    }


}
