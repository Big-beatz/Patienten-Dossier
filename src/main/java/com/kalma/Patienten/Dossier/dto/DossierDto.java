package com.kalma.Patienten.Dossier.dto;

import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.Report;

import java.time.LocalDate;
import java.util.List;

public class DossierDto {

    public Long id;

    public boolean dossierIsClosed;

    public String name;

    public List<Report> reportIds;

}
