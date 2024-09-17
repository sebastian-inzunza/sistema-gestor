package com.sistem.sistema.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.repository.RolesRepository;

@Service
public class RolesService {
    
    @Autowired
    RolesRepository rolesRepository;

    public List<RolesEntity> ObtenerRoles(){
        return rolesRepository.obtenerRoles();
    }

    public List<RolesEntity> ObtenerRolesAdmin(){
        return rolesRepository.findAll();
    }

    public Optional<RolesEntity> ObtenerRolPorNombre(String nombre){
        return rolesRepository.obtenerRolPorNombre(nombre);
    }


}
