package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.repository.DossierRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DossierService {
    private final DossierRepository dossierRepository;

    public DossierService(DossierRepository repository) {
        this.dossierRepository = repository;
    }

    public List<DossierDto> getAllDossiers() {
        List<Dossier> dossiers = dossierRepository.findAll();
        List<DossierDto> dossierDtos = new ArrayList<>();

        for (Dossier dossier : dossiers) {
            dossierDtos.add(dossierToDto(dossier));
        }

        return dossierDtos;
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
