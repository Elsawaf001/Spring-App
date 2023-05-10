package com.elsawaf.app.jwtAuth.domain;

public class UserNameExistException extends Exception{
    public UserNameExistException(String message) {
        super(message);
    }
}
