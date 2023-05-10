package com.elsawaf.app.jwtAuth.utility;

import com.elsawaf.app.jwtAuth.constant.SecurityConstant;
import com.elsawaf.app.jwtAuth.domain.User;
import com.elsawaf.app.jwtAuth.domain.UserPrincipal;
import com.elsawaf.app.jwtAuth.enumeration.Role;
import com.elsawaf.app.jwtAuth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class SuperToken implements CommandLineRunner {
private final JwtTokenProvider tokenProvider;
private final Logger logger = LoggerFactory.getLogger(getClass());
private final UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
superToken();
    }


    private void superToken(){
        User user = new User();
        user.setFirstName("ahmed");
        user.setLastName("Elsawaf");
        user.setUserName("elsawaf001");
        user.setPassword("1234");
        user.setIsNotLocked(true);
        user.setIsActive(true);
        user.setEmail("elsawaf001@gmail.com");
        user.setAuthorities(Role.ROLE_SUPER_USER.getAuthorities());
        user.setRole(Role.ROLE_SUPER_USER.name());
        logger.info("Created Super User With name :- " + user.getUserName() + " and Password :- "+ user.getPassword());
        user.setJoinedDate(new Date());
        user.setUserId("User_01");
        repository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String superToken = tokenProvider.generateJwtToken(userPrincipal);
        logger.info("SUPER TOKEN");
        logger.info(SecurityConstant.TOKEN_PREFIX +superToken);

    }
}
