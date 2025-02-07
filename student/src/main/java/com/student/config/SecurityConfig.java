package com.student.config;

import com.student.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
/*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/student-service/crud/add-update").hasRole(Constants.ADMIN_ROLE)
                        .requestMatchers("/student-service/crud/getAllStudents").hasRole(Constants.ADMIN_ROLE)
                        .requestMatchers("/student-service/crud/getAllStudents").hasAnyRole(Constants.STUDENT_ROLE, Constants.ADMIN_ROLE)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }*/
}
