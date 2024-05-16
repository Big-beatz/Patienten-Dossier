package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.EmployeeService;
import com.kalma.Patienten.Dossier.dto.EmployeeDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    //illegal action add this in the layer
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getField());
                sb.append(": ");
                sb.append(fieldError.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        }
        else {
                employeeDto = employeeService.createEmployee(employeeDto);

                URI uri = URI.create(
                        ServletUriComponentsBuilder.
                                fromCurrentRequest().
                                path("/" + employeeDto.id).
                                toUriString()
                );
                return new ResponseEntity<>(employeeDto, HttpStatus.CREATED);
        }
    }
}