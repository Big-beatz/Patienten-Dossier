package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.DossierDto;
import com.kalma.Patienten.Dossier.dto.ReportDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Patient;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.repository.DossierRepository;
import com.kalma.Patienten.Dossier.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final DossierRepository dossierRepository;
    private final DossierService dossierService;

    public ReportService(ReportRepository repository, DossierRepository dossierRepository, DossierService dossierService) {
        this.reportRepository = repository;
        this.dossierRepository = dossierRepository;
        this.dossierService = dossierService;
    }

    public List<ReportDto> getAllReports() {
        List <Report> reports = reportRepository.findAll();
        List <ReportDto> reportDtos = new ArrayList<>();

        for (Report report : reports) {
            reportDtos.add(reportToDto(report));
        }

        return reportDtos;
    }

    public ReportDto createReport(ReportDto reportDto) {
        Report report = dtoToReport(reportDto);
        reportRepository.save(report);

        reportDto.id = report.getId();
        reportDto.body = report.getBody();
        reportDto.dossierId = report.getDossier().getId();

        //link to patient
        if (reportDto.dossierId != null) {
            Dossier dossierById = dossierRepository.findById(reportDto.dossierId).get();
            if (dossierById.getReports() == null) {
                report.setDossier(dossierById);
                reportRepository.save(report);
            }
        }
        return reportDto;
    }



        //mapping functions
    public Report dtoToReport(ReportDto reportDto) {
        Report report = new Report();

        report.setBody(reportDto.body);
        report.setDate(reportDto.date);
        if(reportDto.dossierId != null) {
            report.setDossier(dossierService.getDossierById(reportDto.dossierId).get());
        }
        return report;
    }

    public ReportDto reportToDto(Report report) {
        ReportDto reportDto = new ReportDto();

        reportDto.id = report.getId();
        reportDto.body = report.getBody();
        reportDto.date = report.getDate();
        reportDto.dossierId = report.getDossier().getId();
        reportDto.employeeId = report.getEmployee().getId();

        return reportDto;
    }
}
