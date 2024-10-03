package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.EndpointsService;
import com.sistem.sistema.services.RolesService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("api/roles")
public class RolesController {
    
    @Autowired
    RolesService rolesService;

    @Autowired
    EndpointsService endpointsService;

    @GetMapping("obtener")
    public List<RolesEntity> ObtenerRoles() {
        List<RolesEntity> roles = rolesService.ObtenerRoles();
        roles.forEach( rol ->{
            rol.setNombre(rol.getNombre().split("_")[1]);
        });
        
        return  roles;
    }

    @GetMapping("obtener/admin")
    public List<RolesEntity> ObtenerRolesAdmin() {
        return rolesService.ObtenerRolesAdmin();
    }
    
   
    @GetMapping("obtener/{id}")
    public RolesEntity ObtenerRol(@PathVariable Long id) {
        RolesEntity rolEncontrado = rolesService.ObtenerRolPorId(id).orElseThrow(() -> new NotFoundException("El rol a editar no existe o fue eliminado"));
        return rolEncontrado;
    }
    

    @PostMapping("crear")
    public ResponseEntity<Object> CrearRoles (@RequestBody RolesEntity rol) {

        String nombre_rol = rol.getNombre();
        rol.setNombre("ROLE_" + rol.getNombre().toUpperCase());

        if(rolesService.isRolExist(rol.getNombre())){
            throw new NotFoundException(String.format("El rol '%s' ya existe", nombre_rol));
        }

        if( rol.getEndpoints() == null || rol.getEndpoints().isEmpty() ){
            throw new NotFoundException("Para poder crear rol debes seleccionar por lo menos una funcionalidad");
        }

        rolesService.CrearRoles(rol);
        return ResponseEntity.ok().body(String.format("El rol '%s' fue creado con exito", nombre_rol));
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Object> EditarRol(@PathVariable Long id, @RequestBody RolesEntity rol) {
        
        RolesEntity rolEncontrado = rolesService.ObtenerRolPorId(id).orElseThrow(() -> new NotFoundException("El rol a editar no existe o fue eliminado"));


        String nombre_rol = rol.getNombre();
        rol.setNombre("ROLE_" + rol.getNombre().toUpperCase());

        if(!rol.getNombre().equals(rolEncontrado.getNombre()) && rolesService.isRolExist(rol.getNombre())){
            throw new NotFoundException(String.format("El rol '%s' ya existe", nombre_rol));
        }

        if( rol.getEndpoints() == null || rol.getEndpoints().isEmpty() ){
            throw new NotFoundException("Para poder crear rol debes seleccionar por lo menos una funcionalidad");
        }

        endpointsService.EliminarEndpointRoles(rolEncontrado.getRolId());
        rolesService.CrearRoles(rol);

        return ResponseEntity.ok().body("Rol editado");
    }

    @GetMapping("obtener/endpoints")
    public List<EnpointsEntity> ObtenerEndpoints () {
        return endpointsService.ObtenerEndpoints();
    }

    
}
