package com.sistem.sistema.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.UsuarioEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.repository.UsuarioRepository;

@Service
public class JpaDetailsService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<UsuarioEntity> usuario = usuarioRepository.obtenerUsuarioPorEmail(email);

        if(!usuario.isPresent()){
            throw new NotFoundException(String.format("El usuario %s no existe en el sistema", email));
        } 

        UsuarioEntity user = usuario.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());


        return new User(
            user.getEmail(), 
            user.getPassword(), 
            user.getEstatus(),
            true, 
            true, 
            true,  
                authorities);
    }
    
}
