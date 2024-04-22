package com.kalma.Patienten.Dossier.dto;

//input & output dto

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientDto {
    public Long id;

    @NotBlank
    public String firstName;

    @Size(min=3, max=128)
    public String lastName;

    public String fullName;
}
