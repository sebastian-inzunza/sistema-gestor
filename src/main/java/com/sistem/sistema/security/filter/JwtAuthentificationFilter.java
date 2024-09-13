package com.sistem.sistema.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistem.sistema.entity.UsuarioEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.sistem.sistema.security.TokenJwtConfig.*;

public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthentificationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    ///login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
            UsuarioEntity usuario = null;
            String username = null;
            String password = null;

            try {
                usuario = new ObjectMapper().readValue(request.getInputStream(), UsuarioEntity.class);
                username = usuario.getEmail();
                password = usuario.getPassword();
            } catch (StreamReadException e) {
                e.printStackTrace();
            } catch (DatabindException e) { 
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
            User user = (User) authResult.getPrincipal();
            String username = user.getUsername();
            Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
            
            Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .build();

            String token = Jwts.builder()
                .subject(username)
                .signWith(SECRET_KEY)                              //6 horas de caducidad 
                .expiration(new Date(System.currentTimeMillis() + (3600000 * 6)))
                .issuedAt(new Date())
                .claims(claims) 
                .compact();

            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            Map<String, Object> body = new HashMap<String, Object>();
            body.put("token", token);
            body.put("username", username);
            body.put("message", String.format("Hola %s haz iniciado sesion con exito", username));

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("message", "Error al iniciar sesion, usuario o contraseña incorrecto" );
            body.put("error", failed.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(401);
    }

    
}
