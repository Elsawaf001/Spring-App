package com.elsawaf.app.jwtAuth.filter;

import com.elsawaf.app.jwtAuth.constant.SecurityConstant;
import com.elsawaf.app.jwtAuth.utility.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

private final JwtTokenProvider jwtTokenProvider  ;
    @Autowired
    public JwtAuthorizationFilter( JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHODE)){
            response.setStatus(HttpStatus.OK.value());
        }
        else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith
                    (SecurityConstant.TOKEN_PREFIX))
            {
                 filterChain.doFilter(request,response);
                 return;
            }
            String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
            String userName = jwtTokenProvider.getSubject(token);
            if (jwtTokenProvider.isTokenValid(token , userName)
                    &&
                    SecurityContextHolder.getContext().getAuthentication()==null)
            {
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthority(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(userName,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                SecurityContextHolder.clearContext();
            }


        }
        filterChain.doFilter(request,response);

    }
}
