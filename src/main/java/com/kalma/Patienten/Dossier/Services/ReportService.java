package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.ReportDto;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository repository) {
        this.reportRepository = repository;
    }

    public List<ReportDto> getAllReports() {
        List <Report> reports = reportRepository.findAll();
        List <ReportDto> reportDtos = new ArrayList<>();

        for (Report report : reports) {
            reportDtos.add(reportToDto(report));
        }

        return reportDtos;
    }


    //mapping functions
    public Report dtoToReport(ReportDto reportDto) {
        Report report = new Report();

        report.setBody(reportDto.body);
        report.setDate(reportDto.date);

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
