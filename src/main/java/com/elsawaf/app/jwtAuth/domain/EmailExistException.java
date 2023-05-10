package com.elsawaf.app.jwtAuth.domain;

public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}
