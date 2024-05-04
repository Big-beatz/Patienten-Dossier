package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.ReportService;
import com.kalma.Patienten.Dossier.dto.ReportDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports(){
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
