package com.kalma.Patienten.Dossier.dto;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

public class ReportDto {
    public Long id;

    public LocalDate date;

    public String body;

    public Long dossierId;

    public Long employeeId;

    public String reportFile;
}
