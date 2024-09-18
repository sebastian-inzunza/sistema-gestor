package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.UsuarioEntity;
import com.sistem.sistema.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioEntity> ObtenerUsuarios (){
        List<UsuarioEntity> usuarioEncontrado = usuarioRepository.obtenerUsuarios();
        return usuarioEncontrado;
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> ObtenerUsuarioId (Long id){
        Optional<UsuarioEntity> usuarioEncontrado = usuarioRepository.obtenerUsuarioPorId(id);
        return usuarioEncontrado;
    }

    @Transactional(readOnly = true)
    public boolean isEmailExist (String email){
        return  usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = false)
    public UsuarioEntity  RegistrarUsuario(UsuarioEntity usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEmail(usuario.getEmail().toLowerCase());
        usuario.setFecha(new Timestamp(new Date().getTime()));
        usuario.setEstatus(true);

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = false)
    public UsuarioEntity  EditarUsuario(UsuarioEntity usuario){
        return usuarioRepository.save(usuario);
    } 
}
