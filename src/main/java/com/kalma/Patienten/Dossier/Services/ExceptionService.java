package com.kalma.Patienten.Dossier.Services;

import com.kalma.Patienten.Dossier.exceptions.*;
import org.springframework.stereotype.Service;


@Service
public class ExceptionService {

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

    public void DeleteNotAllowed(String message) throws DeleteNotAllowed {
        throw new DeleteNotAllowed(message);
    }
}
