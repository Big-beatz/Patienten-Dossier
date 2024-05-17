package com.kalma.Patienten.Dossier.dto;

//input & output dto

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDto {
    public Long id;

    public String firstName;

    public String lastName;

    public String fullName;

    public List<Long> employeeIds = new ArrayList<>();

    public Long dossierId;

    public LocalDate nextAppointment;

}
