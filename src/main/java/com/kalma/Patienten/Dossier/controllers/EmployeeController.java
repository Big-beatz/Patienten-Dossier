package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.Services.EmployeeService;
import com.kalma.Patienten.Dossier.Services.ExceptionService;
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
    private final ExceptionService exceptionService;

    public EmployeeController(EmployeeService employeeService,
                              ExceptionService exceptionService
    ) {
        this.employeeService = employeeService;
        this.exceptionService = exceptionService;
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
                return ResponseEntity.created(uri).body(employeeDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable("id") Long employeeId) {
        //check if input is valid
        if(employeeId == null || employeeId <= 0) {
            exceptionService.InputNotValidException("Employee id is required");
        }
        try {
            //delete employee
            String deletedMessage = employeeService.deleteEmployee(employeeId);

            return ResponseEntity.ok(deletedMessage);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
}