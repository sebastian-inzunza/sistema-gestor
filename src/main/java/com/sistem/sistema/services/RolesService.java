package com.sistem.sistema.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.repository.RolesRepository;

@Service
public class RolesService {
    
    @Autowired
    RolesRepository rolesRepository;


    @Transactional(readOnly = true)
    public Optional<RolesEntity> ObtenerRolPorId(Long id){
        return rolesRepository.obtenerRolPorId(id);
    }

    @Transactional(readOnly = true)
    public List<RolesEntity> ObtenerRoles(){
        return rolesRepository.obtenerRoles();
    }

    @Transactional(readOnly = true)
    public List<RolesEntity> ObtenerRolesAdmin(){
        return rolesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RolesEntity> ObtenerRolPorNombre(String nombre){
        return rolesRepository.obtenerRolPorNombre(nombre);
    }

    public RolesEntity CrearRoles(RolesEntity rol){
        return rolesRepository.save(rol);
    }

    @Transactional(readOnly = true)
    public boolean isRolExist (String nombre_rol){
        return rolesRepository.existsByNombre(nombre_rol);
    }


    public void EliminarRoles(Long usuarioId){
        rolesRepository.eliminarUsuariosRoles(usuarioId);
    }


}
