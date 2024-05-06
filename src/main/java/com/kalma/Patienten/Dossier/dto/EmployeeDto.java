package com.kalma.Patienten.Dossier.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class EmployeeDto {
    public Long id;

    public String firstName;

    @NotBlank
    public String lastName;

    public String fullName;

    @NotBlank
    public String role;

    public List<Long> patientIds;

}
