package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.dto.ReportDto;
import com.kalma.Patienten.Dossier.models.Dossier;
import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Report;
import com.kalma.Patienten.Dossier.repository.ReportRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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


    public ReportDto createManualReport(MultipartFile file, String dossierName, LocalDate date, String token) {
        //initialise
        ReportDto reportDto = new ReportDto();

        reportDto.reportFile = uploadMamualReport(file);

        //check if user is allowed to create patient
        Employee employee = employeeService.getEmployeeByToken(token);

        //getDossier
        Dossier dossier = dossierService.getDossierByName(dossierName);

        //check if dossier is not closed
        if(dossier.getDossierIsClosed()) {
            exceptionService.AddingReportNotAllowedException("Dossier with name " + dossierName + " is closed");
        }

        reportDto.date = date;
        reportDto.dossierId = dossier.getId();
        reportDto.employeeId = employee.getId();

        //make report
        Report report = dtoToReport(reportDto);
        reportRepository.save(report);

        reportDto.id = report.getId();

        return reportDto;

    }

    public String uploadMamualReport(MultipartFile file) {
        //check if file is .pdf
        String fileName = file.getOriginalFilename();
        if(!fileName.contains(".pdf")){
            exceptionService.InputNotValidException("File name should end with '.pdf'");
        }
        String filePath = System.getProperty("user.dir") + "/uploads" + File.separator + file.getOriginalFilename();

        try {
            FileOutputStream fout = new FileOutputStream(filePath);
            fout.write(file.getBytes());

            fout.close();
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
        return fileName;
    }


    public ResponseEntity downloadReportFile(String fileName){
        String fileUploadpath = System.getProperty("user.dir") + "/uploads";

        File directory = new File(fileUploadpath);
        File[] files = directory.listFiles();

        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }

        boolean contains = fileNames.contains(fileName);
        if(!contains) {
            exceptionService.FileNotFoundException("FIle Not Found");
        }

        String filePath = fileUploadpath+File.separator+fileName;

        File file= new File(filePath);

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception exception){
            exception.printStackTrace();
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
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
        //setReportFile
        report.setReportFile(reportDto.reportFile);
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
