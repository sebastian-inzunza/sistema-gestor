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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("api/usuario")
public class UsuariosController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolesService rolesService;

    @GetMapping("obtener")
    public Page<UsuarioEntity> ObtenerUsuarios(  
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer limit
    ) {
        Page<UsuarioEntity> usuario = usuarioService.ObtenerUsuariosPaginado(page, limit);
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
    
    @PutMapping("editar/{id}")
    public ResponseEntity<Object> EditarUsuario(@PathVariable Long id, @RequestBody UsuarioEntity usuario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(!usuario.getEmail().equals(auth.getPrincipal())){
            throw new NotFoundException("Este usuario no puede ser editado o no cuentas con los permisos necesarios");
        }

        UsuarioEntity usuarioEncontrado = usuarioService.ObtenerUsuarioId(id).orElseThrow(() -> new NotFoundException("Usuario no encontado"));
        usuarioService.EditarUsuario(usuarioEncontrado);
        return ResponseEntity.ok().body("Usuario editado");
    }

    //Restaurar contraseña
    

    @PutMapping("editar/rol/{id}")
    public ResponseEntity<Object> EditarRolUsuario(@PathVariable Long id, @RequestBody UsuarioEntity usuario) {

        UsuarioEntity usuarioEncontrado = usuarioService.ObtenerUsuarioId(id).orElseThrow(() -> new NotFoundException("Usuario no encontado"));

        if(usuario.getRoles() == null || usuario.getRoles().isEmpty()){
            throw new NotFoundException("El usuario debe de tener por lo menos 1 rol asignado");
        }
        usuarioService.EditarRolUsuario(usuarioEncontrado);
        return ResponseEntity.ok().body("Roles de usuarios editados");
    }
}
