package com.kalma.Patienten.Dossier.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {
        super();
    }
    public RecordNotFoundException (String message) {
        super(message);
    }
}
