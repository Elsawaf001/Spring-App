package com.elsawaf.supportportal;
import com.elsawaf.supportportal.constant.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;

@SpringBootApplication
public class SupportPortalApplication {

    public static void main(String[] args) {

        SpringApplication.run(SupportPortalApplication.class, args);
  new File(FileConstant.USER_FOLDER).mkdirs();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
