package com.sistem.sistema.security;

import java.util.List;

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

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.security.filter.JwtAuthentificationFilter;
import com.sistem.sistema.security.filter.JwtValidationFilter;
import com.sistem.sistema.services.EndpointsService;

@Configuration
public class SpringSecurityConfig{

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    EndpointsService endpointsService;

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
        List<EnpointsEntity> endpoints = endpointsService.ObtenerEndpoints();
        
        return http.authorizeHttpRequests(
            (authz)->  {
                endpoints.forEach(path ->{
                    if(!path.getRoles().isEmpty()){
                        String[] roles = path.getRoles().stream().map(RolesEntity::getNombre)
                                        .map(name->{
                                            return name.split("_")[1]; 
                                        })
                                        .toArray(String[]::new);
                        authz.requestMatchers(HttpMethod.valueOf(path.getMethod()), path.getUrl()).hasAnyRole(roles);
                    }
                });
                authz
                    .requestMatchers(HttpMethod.POST, "/api/usuario/crear").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/usuario/crear/admin").hasRole("ADMIN")
                    .anyRequest().authenticated();
                }
            )
            .addFilter(new JwtAuthentificationFilter(authenticationManager()))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .csrf(config -> config.disable())
            .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }
}
