package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NameAlreadyBoundException;

@ControllerAdvice
public class ExceptionController {
    //todo check if these are even used
    @ExceptionHandler (value = RecordNotFoundException.class)
    public ResponseEntity<Object> recordNotFoundException(RecordNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    //todo check if these are even used
    @ExceptionHandler (value = InputNotValidException.class)
    public ResponseEntity<Object> inputNotValidException(InputNotValidException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> usernameAlreadyExists(UsernameAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = AddingReportNotAllowedException.class)
    public ResponseEntity<Object> AddingReportNotAllowedException(AddingReportNotAllowedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = InvalidFunctionException.class)
    public ResponseEntity<Object> NotASecretaryException(InvalidFunctionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = DeleteNotAllowed.class)
    public ResponseEntity<Object> DeleteNotAllowed(InvalidFunctionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
}

