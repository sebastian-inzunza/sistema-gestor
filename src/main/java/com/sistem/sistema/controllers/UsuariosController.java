package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.entity.UsuarioEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.RolesService;
import com.sistem.sistema.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("api/usuario")
public class UsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolesService rolesService;


    @GetMapping("obtener")
    public List<UsuarioEntity> ObtenerUsuarios(  
    ) {
        List<UsuarioEntity> usuario = usuarioService.ObtenerUsuarios();
        return usuario;  
    }

    @GetMapping("obtener/{id}")
    public UsuarioEntity ObtenerUsuarioId(
        @PathVariable(name = "id") Long id    
    ) {
        UsuarioEntity usuario = usuarioService.ObtenerUsuarioId(id).orElseThrow(() -> new NotFoundException("No se encontro el usuario"));
        return usuario;  
    }

    @PostMapping("crear")
    public ResponseEntity<Object> CrearUsuario(@RequestBody UsuarioEntity entity) {
        
        if(usuarioService.isEmailExist(entity.getEmail())){
            throw new NotFoundException("El email " + entity.getEmail() + " ya se encuentra registrado");
        }

        Optional<RolesEntity> rol = rolesService.ObtenerRolPorNombre("ROLE_USER");
        List<RolesEntity> roles = new ArrayList<>();
        rol.ifPresent(roles::add);
        entity.setRoles(roles);
        
        

        UsuarioEntity usuario = usuarioService.RegistrarUsuario(entity);
        return ResponseEntity.ok().body("Usuario " + usuario.getNombre() + " registrado con exito");
    }
    
    @PostMapping("crear/admin")
    public ResponseEntity<Object> CrearUsuarioAdmin(@RequestBody UsuarioEntity entity) {

        if(usuarioService.isEmailExist(entity.getEmail())){
            throw new NotFoundException("El email " + entity.getEmail() + " ya se encuentra registrado");
        }

        Optional<RolesEntity> rol = rolesService.ObtenerRolPorNombre("ROLE_USER");
        List<RolesEntity> roles = new ArrayList<>();
        rol.ifPresent(roles::add);

        if(entity.getIsAdmin()){
            Optional<RolesEntity> rolAdmin = rolesService.ObtenerRolPorNombre("ROLE_ADMIN");
            rolAdmin.ifPresent(roles::add);
        }

        entity.setRoles(roles);

        UsuarioEntity usuario = usuarioService.RegistrarUsuario(entity);
        
        return ResponseEntity.ok().body("Administrador " + usuario.getNombre() + " registrado con exito");
    }
    
    
}
