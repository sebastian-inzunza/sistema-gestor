package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.CategoriasEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.CategoriasService;


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
@RequestMapping("api/categorias")
public class CategoriasController {
    
    @Autowired
    CategoriasService categoriasService;

    @GetMapping("obtener")
    public List<CategoriasEntity> ObtenerCategorias() {
        return categoriasService.obtenerCategorias();
    }

    @PostMapping("crear")
    public ResponseEntity<Object> CrearCategoria(@RequestBody CategoriasEntity entity) {

        if(categoriasService.isExistcategoria(entity.getNombre())){
            throw new NotFoundException(String.format("La categoria con el nombre '%s' ya existe", entity.getNombre()));
        }

        CategoriasEntity categoria = categoriasService.crearCategoria(entity);
        return ResponseEntity.ok().body(String.format("La categoria '%s' fue creado con exito", categoria.getNombre()));
    }
    
    @PutMapping("edita/{id}")
    public ResponseEntity<Object> EditarCategoria(@PathVariable Long id, @RequestBody CategoriasEntity categoria) {        
        CategoriasEntity categoriaEncontrada = categoriasService.obtenerCategoriaPorId(id).orElseThrow(() -> new NotFoundException("No se encontro categoria"));

        if (!categoriaEncontrada.getNombre().equals(categoria.getNombre()) && categoriasService.isExistcategoria(categoria.getNombre())) {
            throw new NotFoundException(String.format("La categoria '%s' ya existe", categoria.getNombre()));
        }

        categoriaEncontrada.setNombre(categoria.getNombre());
        categoriasService.editarCategoria(categoriaEncontrada);

        return ResponseEntity.ok().body("Categora editada");
    }
    
}
