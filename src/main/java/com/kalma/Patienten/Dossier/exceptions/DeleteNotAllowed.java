package com.kalma.Patienten.Dossier.exceptions;

public class DeleteNotAllowed extends RuntimeException{
    public DeleteNotAllowed(String message) {
        super(message);
    }
}
