package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.exceptions.*;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import org.springframework.stereotype.Service;


@Service
public class ExceptionService {
    private final EmployeeRepository employeeRepository;

    public ExceptionService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void InvalidFunctionException(String message) throws InvalidFunctionException {
        throw new InvalidFunctionException(message);
    }

    public void UsernameAlreadyExistsException(String username) throws UsernameAlreadyExistsException {
        throw new UsernameAlreadyExistsException("Username " + username + " already exists");
    }


    public void InputNotValidException(String message) throws InputNotValidException {
        throw new InputNotValidException(message);
    }

    public void RecordNotFoundException(String message) throws RecordNotFoundException {
        throw new RecordNotFoundException(message);
    }

    public void AddingReportNotAllowedException(String message) throws AddingReportNotAllowedException {
        throw new AddingReportNotAllowedException(message);
    }




}
