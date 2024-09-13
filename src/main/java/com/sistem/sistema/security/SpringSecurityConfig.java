package com.sistem.sistema.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sistem.sistema.security.filter.JwtAuthentificationFilter;
import com.sistem.sistema.security.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig{

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager () throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests((authz)-> authz
            .requestMatchers(HttpMethod.GET, "/api/usuario").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/usuario").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/usuario/admin").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/usuario/admin").hasRole("ADMIN")
                .anyRequest().authenticated())
            .addFilter(new JwtAuthentificationFilter(authenticationManager()))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .csrf(config -> config.disable())
            .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}
