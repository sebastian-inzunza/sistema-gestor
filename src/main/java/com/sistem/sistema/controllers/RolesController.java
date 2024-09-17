package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.services.EndpointsService;
import com.sistem.sistema.services.RolesService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

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
    
    
}
