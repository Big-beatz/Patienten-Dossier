package com.kalma.Patienten.Dossier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

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

    public String username;

    @NotBlank
    public String password;

    @NotEmpty
    public List<String> roles = new ArrayList<>();
}
