package com.elsawaf.app.jwtAuth.domain;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
