package com.kalma.Patienten.Dossier.dto;

import java.time.LocalDate;

public class ReportDto {
    public Long id;

    public LocalDate date;

    public String body;

    public Long dossierId;

    public Long employeeId;

    public String reportFile;
}
