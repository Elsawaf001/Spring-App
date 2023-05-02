package com.elsawaf.supportportal.constant;

public class SecurityConstant {
    public static final Long EXPIRATION_TIME= 432_000_000l; //5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
public static final String ELSAWAF_COMPANY ="Ahmed Elsawaf Company";
public static final String ELSAWAF_ADMINISTRATION = "AHMED ELSAWAF AD";
public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
public static final String OPTIONS_HTTP_METHODE = "Options";
//public static final String[] PUBLIC_URLS = {"/user/login" , "/user/register" , "/user/resetpassword/**" , "/user/image/**"};
public static final String AUTHORITIES = "authorities";
    public static final String[] PUBLIC_URLS = {"**"};
}
