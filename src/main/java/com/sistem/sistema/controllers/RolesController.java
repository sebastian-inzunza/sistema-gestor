package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.services.EndpointsService;
import com.sistem.sistema.services.RolesService;

import jakarta.ws.rs.NotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/roles")
public class RolesController {
    
    @Autowired
    RolesService rolesService;

    @Autowired
    EndpointsService endpointsService;

    @GetMapping("obtener")
    public List<RolesEntity> ObtenerRoles() {
        return rolesService.ObtenerRoles();
    }

    @GetMapping("obtener/admin")
    public List<RolesEntity> ObtenerRolesAdmin() {
        return rolesService.ObtenerRolesAdmin();
    }
    
    @GetMapping("obtener/endpoints")
    public List<EnpointsEntity> ObtenerEmdpoints () {
        return endpointsService.ObtenerEndpoints();
    }
    

    @PostMapping("crear")
    public ResponseEntity<Object> CrearRoles (@RequestBody RolesEntity rol) {

        String nombre_rol = rol.getNombre();
        rol.setNombre("ROLE_" + rol.getNombre().toUpperCase());

        if(rolesService.isRolExist(rol.getNombre())){
            throw new NotFoundException(String.format("El rol '%s' ya existe", nombre_rol));
        }

        rolesService.CrearRoles(rol);
        return ResponseEntity.ok().body(String.format("El rol '%s' fue creado con exito", nombre_rol));
    }
    
    
}
