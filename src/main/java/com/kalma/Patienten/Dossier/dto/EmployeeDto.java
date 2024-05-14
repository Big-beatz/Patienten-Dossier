package com.kalma.Patienten.Dossier.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    public List<Long> patientIds = new ArrayList<>();

    public String[] roles;
}
