package com.elsawaf.app.jwtAuth.domain;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {

    public static final String ERROR_PATH = "/error";
@RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404(){
    return createHttpResponse(HttpStatus.NOT_FOUND , "this Page not Found 404 ERROR");
}
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ACCOUNT_LOCKED = "Your Account Has Been Locked , Please Contact Administration";
    public static final String METHOD_IS_NOT_ALLOWED = "This request methode is not allowed on this endPoint , " +
            "please send a '%s' request";
    public static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred , please try again";
    public static final String INCORRECT_CREDENTIALS = "User name / Password incorrect , please try again";
    public static final String ACCOUNT_DISABLED = "your account has been disabled , if this is an error please contact administration ";
    public static final String ERROR_PROCESSING_FILE = "Error occurred while processing the file";
public static final String NOT_ENOUGH_PERMISSION = "You don't have enough permission";


    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus , String message){
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase() );
        return new ResponseEntity<>(httpResponse , httpStatus);
    }

@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<HttpResponse> badCredentialException(BadCredentialsException exception){
    LOGGER.error(exception.getMessage());
    return createHttpResponse(HttpStatus.BAD_REQUEST , INCORRECT_CREDENTIALS);
}


@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<HttpResponse> accessDeniedException(){
    return createHttpResponse(HttpStatus.FORBIDDEN , NOT_ENOUGH_PERMISSION);
}


@ExceptionHandler(LockedException.class)
public ResponseEntity<HttpResponse> lockedException(){
    return createHttpResponse(HttpStatus.LOCKED , ACCOUNT_LOCKED);
}


@ExceptionHandler(TokenExpiredException.class)
public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException e){
    return createHttpResponse(HttpStatus.UNAUTHORIZED , e.getMessage().toUpperCase());
}


@ExceptionHandler(EmailExistException.class)
public ResponseEntity<HttpResponse> emailExistException(EmailExistException e){
    return createHttpResponse(HttpStatus.BAD_REQUEST , e.getMessage().toUpperCase());
}


@ExceptionHandler(UserNameExistException.class)
public ResponseEntity<HttpResponse> userNameExistException(UserNameExistException e){
    return createHttpResponse(HttpStatus.BAD_REQUEST , e.getMessage().toUpperCase());
}


@ExceptionHandler(UserNotFoundException.class)
public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException e){
    return createHttpResponse(HttpStatus.BAD_REQUEST , e.getMessage().toUpperCase());
}


@ExceptionHandler(EmailNotFoundException.class)
public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException e){
    return createHttpResponse(HttpStatus.BAD_REQUEST , e.getMessage().toUpperCase());
}


@ExceptionHandler(DisabledException.class)
public ResponseEntity<HttpResponse> accountDisabledException(){
    return createHttpResponse(HttpStatus.FORBIDDEN , ACCOUNT_DISABLED);
}


@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
public ResponseEntity<HttpResponse> methodeNotSupportedException(HttpRequestMethodNotSupportedException e){
    HttpMethod supportedHttpMethod = Objects.requireNonNull(e.getSupportedHttpMethods()).stream().iterator().next();
    return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED , String.format(METHOD_IS_NOT_ALLOWED , supportedHttpMethod));
}


@ExceptionHandler(Exception.class)
public ResponseEntity<HttpResponse> internalServerErrorException(Exception e){
    LOGGER.error(e.getMessage());
    return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR , INTERNAL_SERVER_ERROR_MSG);
}


@ExceptionHandler(NoResultException.class)
public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
    LOGGER.error(exception.getMessage());
    return createHttpResponse(HttpStatus.NOT_FOUND , exception.getMessage());
}


@ExceptionHandler(IOException.class)
public ResponseEntity<HttpResponse> ioException(IOException ioException){
    LOGGER.error(ioException.getMessage());
    return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR , ERROR_PROCESSING_FILE);
}



}
