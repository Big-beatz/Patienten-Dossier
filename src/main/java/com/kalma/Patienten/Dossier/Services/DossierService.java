package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.models.Report;
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
    private final ExceptionService exceptionService;
    private final EmployeeService employeeService;

    public DossierService(DossierRepository repository,
                          PatientRepository patientRepository,
                          ExceptionService exceptionService,
                          EmployeeService employeeService
    ) {
        this.dossierRepository = repository;
        this.patientRepository = patientRepository;
        this.exceptionService = exceptionService;
        this.employeeService = employeeService;
    }

    public DossierDto createDossier(Long patientId, DossierDto dossierDto) {
        Dossier dossier = dtoToDossier(dossierDto);

        //link to patient done like this to prevent circular dependency
        if(patientId != null) {
            Patient patientById = patientRepository.findById(patientId).get();
            if (patientById.getDossier() == null){
                dossierDto.name = patientById.getFullName();
                patientById.setDossier(dossier);
                patientRepository.save(patientById);
            }
            else {
                exceptionService.RecordNotFoundException("patient not found");
            }
        }

        dossierRepository.save(dossier);

        dossierDto.id = dossier.getId();
        dossierDto.dossierIsClosed = dossier.getDossierIsClosed();

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

    public Dossier getDossierByName(String name) {
        Optional<Dossier> optionalDossier = dossierRepository.findByName(name);
        Dossier dossier = new Dossier();

        if (optionalDossier.isPresent()) {
            dossier = optionalDossier.get();
        } else {
            exceptionService.RecordNotFoundException("Dossier can not be found");
        }
        return dossier;
    }

    public boolean closeOrOpenDossier(Long dossierId, String token) {
        Employee employee = employeeService.getEmployeeByToken(token);
        //check if employee is secretary
        employeeService.checkIfUserIsSecretary(employee);

        //find dossier
        Dossier dossier = getDossierById(dossierId);

        //change value
        boolean newValueDossierIsClosed = !dossier.getDossierIsClosed();
        dossier.setDossierIsClosed(newValueDossierIsClosed);
        dossierRepository.save(dossier);

        return newValueDossierIsClosed;
    }

    public Dossier getDossierById(Long id) {
        Optional<Dossier> optionalDossier = dossierRepository.findById(id);
        Dossier dossier = new Dossier();

        if (optionalDossier.isPresent()) {
            dossier = optionalDossier.get();
        } else {
            exceptionService.RecordNotFoundException("Dossier can not be found");
        }
        return dossier;
    }


    public List<Long> getReportIdList(Dossier dossier) {
        List<Long> reportIdList = new ArrayList();
        for(Report report : dossier.getReports()){
            reportIdList.add(report.getId());
        }
        return reportIdList;
    }

    //mapping functions
    public DossierDto dossierToDto(Dossier dossier) {
        DossierDto dossierDto = new DossierDto();

        dossierDto.id = dossier.getId();
        dossierDto.name = dossier.getName();
        dossierDto.dossierIsClosed = dossier.getDossierIsClosed();
        if(dossier.getReports() != null) {
            dossierDto.reportIds = getReportIdList(dossier);
        }

        return dossierDto;
    }

    public Dossier dtoToDossier(DossierDto dossierDto) {
        Dossier dossier = new Dossier();

        dossier.setId(dossierDto.id);
        dossier.setName(dossierDto.name);
        dossier.setDossierIsClosed(dossierDto.dossierIsClosed);

        return dossier;
    }


}
