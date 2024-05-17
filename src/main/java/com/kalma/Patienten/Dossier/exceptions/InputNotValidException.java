package com.kalma.Patienten.Dossier.exceptions;

public class InputNotValidException extends RuntimeException{
    public InputNotValidException(String message) {
        super(message);
    }
}
