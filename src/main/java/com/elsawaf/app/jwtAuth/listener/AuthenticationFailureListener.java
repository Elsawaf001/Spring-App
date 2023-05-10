package com.elsawaf.app.jwtAuth.listener;

import com.elsawaf.app.jwtAuth.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFailureListener  {

    @Autowired
private HttpServletRequest httpServletRequest;





    public final Logger logger = LoggerFactory.getLogger(AuthenticationFailureListener.class);
    private final LoginAttemptService loginAttemptService;



@Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;

}


    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        try {
            logger.info("I have listen to failure event");
            Object principle = event.getAuthentication().getPrincipal();
            if (principle instanceof String){
                String username = (String) event.getAuthentication().getPrincipal();

                loginAttemptService.addUserToLoginAttemptCache(username);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the authentication failure event", e);
        }
    }

}
//@EventListener
//    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
//logger.info("I have listen to failure event");
//    Object principle = event.getAuthentication().getPrincipal();
//    if (principle instanceof String){
//        String username = (String) event.getAuthentication().getPrincipal();
//
//        loginAttemptService.addUserToLoginAttemptCache(username);
//    }
//    }

//    @Override
//    @EventListener
//    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//        logger.info("I have listen to failure event");
//Object principal =event.getAuthentication().getPrincipal();
//if (principal instanceof String){
//    String username = (String) event.getAuthentication().getPrincipal();
//    loginAttemptService.addUserToLoginAttemptCache(username);
//}
//    }
