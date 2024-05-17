package com.kalma.Patienten.Dossier.exceptions;

public class AddingReportNotAllowedException extends RuntimeException {
    public AddingReportNotAllowedException(String message) {
        super(message);
    }
}
