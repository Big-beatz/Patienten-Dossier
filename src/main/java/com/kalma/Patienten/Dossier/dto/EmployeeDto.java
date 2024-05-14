package com.kalma.Patienten.Dossier.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDto {
    public Long id;

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @NotBlank
    public String function;

    public String userName;

    @NotBlank
    public String password;

    public List<Long> patientIds = new ArrayList<>();

    public String[] roles;
}
