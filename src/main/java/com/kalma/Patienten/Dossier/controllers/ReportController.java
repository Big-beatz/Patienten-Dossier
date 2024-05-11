package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.ReportService;
import com.kalma.Patienten.Dossier.dto.ReportDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    //todo add employee ID through authentication.
    @PostMapping("/{dossierId}")
    public ResponseEntity<Object> createReport(@RequestParam Long dossierId, @RequestBody ReportDto reportDto, BindingResult br){
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : br.getFieldErrors()) {
                sb.append(fieldError.getField());
                sb.append(": ");
                sb.append(fieldError.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            reportDto = reportService.createReport(dossierId, reportDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("/" + reportDto.id).
                            toUriString());

            return ResponseEntity.created(uri).body(reportDto);
        }
    }
}
