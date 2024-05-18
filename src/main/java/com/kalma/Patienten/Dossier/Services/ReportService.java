package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.ReportDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.repository.ReportRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final DossierService dossierService;
    private final EmployeeService employeeService;
    private final ExceptionService exceptionService;

    public ReportService(ReportRepository repository, DossierService dossierService, EmployeeService employeeService, ExceptionService exceptionService) {
        this.reportRepository = repository;
        this.dossierService = dossierService;
        this.employeeService = employeeService;
        this.exceptionService = exceptionService;
    }

    public List<ReportDto> getAllReports() {
        List <Report> reports = reportRepository.findAll();
        List <ReportDto> reportDtos = new ArrayList<>();

        for (Report report : reports) {
            reportDtos.add(reportToDto(report));
        }

        return reportDtos;
    }

    public ReportDto createReport(String body, String dossierName, LocalDate date, String token) {
        //initialise
        ReportDto reportDto = new ReportDto();

        //check if user is allowed to create patient
        Employee employee = employeeService.getEmployeeByToken(token);

        //getDossier
        Dossier dossier = dossierService.getDossierByName(dossierName);

        //check if dossier is not closed
        if(dossier.getDossierIsClosed()) {
            exceptionService.AddingReportNotAllowedException("Dossier with name " + dossierName + " is closed");
        }

        //setReportDto
        reportDto.body = body;
        reportDto.date = date;
        reportDto.dossierId = dossier.getId();
        reportDto.employeeId = employee.getId();

        //make report
        Report report = dtoToReport(reportDto);
        reportRepository.save(report);

        reportDto.id = report.getId();

        return reportDto;
        }



        //mapping functions
    public Report dtoToReport(ReportDto reportDto) {
        Report report = new Report();

        report.setBody(reportDto.body);
        report.setDate(reportDto.date);
        if(reportDto.dossierId != null) {
            report.setDossier(dossierService.getDossierById(reportDto.dossierId));
        }
        if(reportDto.employeeId != null) {
            report.setEmployee(employeeService.getEmployeeById(reportDto.employeeId));
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
