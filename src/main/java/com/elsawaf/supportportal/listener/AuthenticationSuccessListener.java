package com.elsawaf.supportportal.listener;


import com.elsawaf.supportportal.domain.UserPrincipal;
import com.elsawaf.supportportal.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationSuccessListener {
    private final LoginAttemptService loginAttemptService;
@Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }
@EventListener
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {

    Object principle = event.getAuthentication().getPrincipal();
    if (principle instanceof UserPrincipal){
        UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
        loginAttemptService.evictUserFromLoginAttempts(user.getUsername());
    }

}
}
