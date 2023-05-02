package com.elsawaf.supportportal.configuration;

import com.elsawaf.supportportal.constant.SecurityConstant;
import com.elsawaf.supportportal.filter.JwtAccessDeniedHandler;
import com.elsawaf.supportportal.filter.JwtAuthenticationEntryPoint;
import com.elsawaf.supportportal.filter.JwtAuthorizationFilter;
import com.elsawaf.supportportal.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
private final UserDetailsService userDetailsService;
private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
private final JwtAuthorizationFilter jwtAuthorizationFilter;
private final BCryptPasswordEncoder bCryptPasswordEncoder;




    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http)  {


        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(SecurityConstant.PUBLIC_URLS)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthorizationFilter , UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .logout()
                .logoutUrl("/")
                .invalidateHttpSession(true);



        return http.build();

    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
return authenticationProvider;
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }




}
//    public AuthenticationManagerBuilder configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    return authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder).and();
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(){
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
//        return new ProviderManager(authProvider);
//    }




//        http.csrf().disable()
//                .cors().and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();