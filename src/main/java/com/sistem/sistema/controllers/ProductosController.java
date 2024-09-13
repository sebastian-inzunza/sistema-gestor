package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.ProductosEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.ProductosSevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/productos")
public class ProductosController {
    
    @Autowired
    ProductosSevice productosSevice;

    @GetMapping("")
    public List<ProductosEntity> obtenerProductos() {
        List<ProductosEntity> productos = productosSevice.obtenerProductos();
        return productos;
    }

    @GetMapping("{id}")
    public ProductosEntity ObtenerProducto(
        @PathVariable(name = "id") Long productoId ) {
        
        ProductosEntity usuarioEncontrado = productosSevice.obtenerProductoPorId(productoId).orElseThrow(() -> new NotFoundException("No se encontro el producto"));
        return usuarioEncontrado;
    }

    @PostMapping("")
    public ResponseEntity<Object> CrearProductos(@RequestBody ProductosEntity productosEntity) {
        System.out.println(productosEntity.toString());

        if(productosSevice.isProductExist(productosEntity.getNombre())){
            throw new NotFoundException(String.format("El producto con nombre '%s'   ya se encuentra registrado", productosEntity.getNombre()));
        }

        ProductosEntity producto = productosSevice.crearProductos(productosEntity);
        return ResponseEntity.ok().body("El producto '" + producto.getNombre() + "'' se ha registrado con exito");
    }
    
}
