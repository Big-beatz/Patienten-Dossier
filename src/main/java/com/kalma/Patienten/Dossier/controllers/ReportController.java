package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.ExceptionService;
import com.kalma.Patienten.Dossier.Services.ReportService;
import com.kalma.Patienten.Dossier.dto.ReportDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;
    private final ExceptionService exceptionService;

    public ReportController(ReportService reportService,
                            ExceptionService exceptionService
    ) {
        this.reportService = reportService;
        this.exceptionService = exceptionService;
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports(){
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PostMapping
    public ResponseEntity<Object> createReport(@Valid @RequestParam("body") String body,
                                               @RequestParam("dossier_name") String dossierName,
                                               @RequestParam("date") String dateString,
                                               @RequestHeader("Authorization") String token)
    {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException exception) {
            exceptionService.InputNotValidException("Date should be formated YYYY-mm-dd");
        }
        if(dossierName == null || dossierName.isEmpty()){
            exceptionService.InputNotValidException("Dossier_name is required");
        }
        if(body == null || body.isEmpty()) {
            exceptionService.InputNotValidException("Body is required");
        }
        if(body != null && body.length() >= 1000) {
            exceptionService.InputNotValidException("Body can be only 1000 characters long");
        } else {
            ReportDto reportDto = reportService.createReport(
                    body,
                    dossierName,
                    date,
                    token
            );

            URI uri = URI.create(
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/" + reportDto.id).
                            toUriString());

            return ResponseEntity.created(uri).body(reportDto);
        }
        return null;
    }

    @PostMapping("/manual")
    public ResponseEntity<Object> createManualReport(@Valid @RequestParam("file") MultipartFile file,
                                                     @RequestParam("dossier_name") String dossierName,
                                                     @RequestHeader("Authorization") String token,
                                                     @RequestParam("date") String dateString
                                                     ){
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException exception) {
            exceptionService.InputNotValidException("Date should be formated YYYY-mm-dd");
        }
        if(dossierName == null || dossierName.isEmpty()){
            exceptionService.InputNotValidException("Dossier_name is required");
        }
        if(file.isEmpty()) {
            exceptionService.InputNotValidException("File is not allowed");
        }
       else {
            ReportDto reportDto = reportService.createManualReport(
                    file,
                    dossierName,
                    date,
                    token
            );

            URI uri = URI.create(
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/" + reportDto.id).
                            toUriString());

            return ResponseEntity.created(uri).body(reportDto);
        }
        return null;
    }

    @GetMapping("/download/{path:.+}")
    public ResponseEntity<Object> downloadFile(@PathVariable("path") String filename) {
        return reportService.downloadReportFile(filename);
    }
}