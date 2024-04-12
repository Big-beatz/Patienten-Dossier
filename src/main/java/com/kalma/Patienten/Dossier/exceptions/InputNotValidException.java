package com.kalma.Patienten.Dossier.exceptions;

public class InputNotValidException extends RuntimeException{
    public InputNotValidException() {
        super();
    }
    public InputNotValidException(String message) {
        super(message);
    }
}
