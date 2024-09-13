package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.RolesEntity;
import com.sistem.sistema.services.RolesService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("api/roles ")
public class RolesController {
    
    @Autowired
    RolesService rolesService;

    @GetMapping("")
    public List<RolesEntity> ObtenerRoles() {
        return rolesService.ObtenerRoles();
    }
    
}
