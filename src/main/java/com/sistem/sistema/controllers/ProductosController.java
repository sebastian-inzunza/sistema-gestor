package com.sistem.sistema.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.ProductosEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.CategoriasService;
import com.sistem.sistema.services.ProductosSevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("api/productos")
public class ProductosController {
    
    @Autowired
    ProductosSevice productosSevice;

    @Autowired 
    CategoriasService categoriasService;

    @GetMapping("obtener")
    public List<ProductosEntity> obtenerProductos() {
        List<ProductosEntity> productos = productosSevice.obtenerProductos();
        return productos;
    }

    @GetMapping("obtener/{id}")
    public ProductosEntity ObtenerProducto(
        @PathVariable(name = "id") Long productoId ) {
        
        ProductosEntity usuarioEncontrado = productosSevice.obtenerProductoPorId(productoId).orElseThrow(() -> new NotFoundException("No se encontro el producto"));
        return usuarioEncontrado;
    }

    @PostMapping("crear")
    public ResponseEntity<Object> CrearProductos(@RequestBody ProductosEntity productosEntity) {

        if(productosSevice.isProductExist(productosEntity.getNombre())){
            throw new NotFoundException(String.format("El producto con nombre '%s'   ya se encuentra registrado", productosEntity.getNombre()));
        }

        ProductosEntity producto = productosSevice.crearProductos(productosEntity);
        return ResponseEntity.ok().body("El producto '" + producto.getNombre() + "'' se ha registrado con exito");
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Object> editarProducto(@PathVariable Long id, @RequestBody ProductosEntity producto) {
        
        ProductosEntity productosEncontrados = productosSevice.obtenerProductoPorId(id).orElseThrow(() -> new NotFoundException("No se encontro el usuario"));

        if(!producto.getNombre().equals(productosEncontrados.getNombre()) && productosSevice.isProductExist(producto.getNombre())){
            throw new NotFoundException(String.format("El producto '%s' ya se encuentra registrado", producto.getNombre()));
        }

        productosEncontrados.setNombre(producto.getNombre());
        productosEncontrados.setPrecio(producto.getPrecio());
        productosEncontrados.setDescripcion(producto.getDescripcion());
        
        categoriasService.eliminarProductoCategoria(productosEncontrados.getProductoId());

        productosEncontrados.setCategorias(producto.getCategorias());
        productosSevice.editarProductos(productosEncontrados);
        
        return ResponseEntity.ok().body("Se editó el producto");
    }
    

    @PutMapping("estatus/{id}")
    public ResponseEntity<Object> putMethodName(@PathVariable Long id, @RequestParam(required = true) Boolean estatus) {
        productosSevice.cambiarEstatus(id, estatus);
        return ResponseEntity.ok().body(estatus? "Producto Activado" :"Producto Desativado");
    }
}
