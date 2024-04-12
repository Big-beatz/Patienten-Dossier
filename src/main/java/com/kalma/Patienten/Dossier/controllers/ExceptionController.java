package com.kalma.Patienten.Dossier.controllers;

import com.kalma.Patienten.Dossier.exceptions.InputNotValidException;
import com.kalma.Patienten.Dossier.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler (value = RecordNotFoundException.class)
    public ResponseEntity<Object> recordNotFoundException(RecordNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler (value = InputNotValidException.class)
    public ResponseEntity<Object> inputNotValidException(InputNotValidException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

}
