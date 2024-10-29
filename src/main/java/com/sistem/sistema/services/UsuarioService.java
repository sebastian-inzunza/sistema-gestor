package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    
    @Autowired
    RolesService rolesService;

    @Transactional(readOnly = true)
    public List<UsuarioEntity> ObtenerUsuarios (){
        List<UsuarioEntity> usuarioEncontrado = usuarioRepository.obtenerUsuarios();
        return usuarioEncontrado;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioEntity> ObtenerUsuariosPaginado(Integer page, Integer limit){
        Page<UsuarioEntity> usuarios = usuarioRepository.findAll(PageRequest.of(page, limit));

        return usuarios;
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> ObtenerUsuarioId (Long id){
        Optional<UsuarioEntity> usuarioEncontrado = usuarioRepository.obtenerUsuarioPorId(id);
        return usuarioEncontrado;
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> ObtenerUsuarioEmail (String email){
        Optional<UsuarioEntity> usuarioEncontrado = usuarioRepository.obtenerUsuarioPorEmail(email);
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
        
        usuario.setApellidos(usuario.getApellidos());
        usuario.setNombre(usuario.getNombre());
        
        return usuarioRepository.save(usuario);
    } 

    @Transactional(readOnly = false)
    public UsuarioEntity  EditarRolUsuario(UsuarioEntity usuario){
        
        rolesService.EliminarRoles(usuario.getUsuarioId());
        usuario.setRoles(usuario.getRoles());

        return usuarioRepository.save(usuario);
    } 
}
