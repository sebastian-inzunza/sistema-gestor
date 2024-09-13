package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.CategoriasEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.CategoriasService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/categorias")
public class CategoriasController {
    
    @Autowired
    CategoriasService categoriasService;

    @GetMapping("")
    public List<CategoriasEntity> ObtenerCategorias() {
        return categoriasService.obtenerCategorias();
    }

    @PostMapping("")
    public ResponseEntity<Object> CrearCategoria(@RequestBody CategoriasEntity entity) {

        if(categoriasService.isExistcategoria(entity.getNombre())){
            throw new NotFoundException(String.format("La categoria con el nombre '%s' ya existe", entity.getNombre()));
        }

        CategoriasEntity categoria = categoriasService.crearCategoria(entity);
        return ResponseEntity.ok().body(String.format("La categoria '%s' fue creado con exito", categoria.getNombre()));
    }
    
    
}
