package com.elsawaf.app.jwtAuth.filter;

import com.elsawaf.app.jwtAuth.constant.SecurityConstant;
import com.elsawaf.app.jwtAuth.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request
            , HttpServletResponse response
            , AccessDeniedException exception)
            throws IOException {

        HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED
                , HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase() ,
                SecurityConstant.ACCESS_DENIED_MESSAGE);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream , httpResponse);
        outputStream.flush();
    }
}
