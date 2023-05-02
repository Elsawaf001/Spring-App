package com.elsawaf.supportportal.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
public static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5 ;
public static final int ATTEMPT_INCREMENT = 1;
private final LoadingCache<String , Integer> loginAttemptCache;

    public LoginAttemptService() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key)  {
                        return 0;
                    }
                });
    }
//    public LoginAttemptService() {
//        loginAttemptCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(15, TimeUnit.MINUTES)
//                .maximumSize(100)
//                .build(key -> 0);
//    }

public void evictUserFromLoginAttempts(String username){
        loginAttemptCache.invalidate(username);
}

@SneakyThrows
public void addUserToLoginAttemptCache(String username)  {
    int attempts = 0;
        attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
    loginAttemptCache.put(username, attempts);
}


@SneakyThrows
public boolean hasExceededMaxAttempts(String username)  {
        return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;

}


//    public boolean hasExceededMaxAttempts(String username) {
//        return loginAttemptCache.getIfPresent(username) != null &&
//                loginAttemptCache.getIfPresent(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
//    }
}
