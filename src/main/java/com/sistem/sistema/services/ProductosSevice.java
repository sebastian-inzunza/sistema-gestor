package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.CategoriasEntity;
import com.sistem.sistema.entity.ProductosEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.repository.ProductosRepository;

@Service
public class ProductosSevice {
    
    @Autowired
    ProductosRepository productosRepository;

    @Autowired 
    CategoriasService categoriasService;

    @Transactional(readOnly = true)
    public List<ProductosEntity> obtenerProductos(){
        return productosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ProductosEntity> obtenerProductosPage(Integer page, Integer limit, String categoriaSearch){

        ExampleMatcher matcher = ExampleMatcher.matching()
                                .withMatcher("nombre", new GenericPropertyMatcher().exact());

        ProductosEntity producto = new ProductosEntity();
        CategoriasEntity categoria = new CategoriasEntity();

        if(categoriaSearch != null && !categoriaSearch.equals("")){
            categoria.setNombre(categoriaSearch);
        }

        producto.setCategorias(categoria);
                                
        return productosRepository.findAll(Example.of(producto, matcher), PageRequest.of(page, limit));
    }

    @Transactional(readOnly = true)
    public Optional<ProductosEntity> obtenerProductoPorId(Long productoId){
        return productosRepository.findProductoById(productoId);
    }

    @Transactional(readOnly = true)
    public boolean isProductExist(String nombre){
        return productosRepository.existsByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public boolean isProductExist(Long productId){
        return productosRepository.findProductoById(productId).isPresent();
    }

    @Transactional(readOnly = false)
    public ProductosEntity crearProductos(ProductosEntity producto){
        Double margen = this.calcularMargen(producto.getPrecio(),producto.getCosto());

        producto.setMargen(margen);
        producto.setFecha(new Timestamp(new Date().getTime()));
        producto.setEstatus(true);

        return productosRepository.save(producto);
    }

    @Transactional(readOnly = false)
    public ProductosEntity editarProductos(ProductosEntity producto, ProductosEntity productoEncontrado){
        productoEncontrado.setNombre(producto.getNombre());
        productoEncontrado.setPrecio(producto.getPrecio());
        productoEncontrado.setCosto(producto.getCosto());
        productoEncontrado.setMargen(this.calcularMargen(producto.getPrecio(), producto.getCosto()));

        productoEncontrado.setDescripcion(producto.getDescripcion());
        productoEncontrado.setPreparado(producto.getPreparado());

        
        if(!productoEncontrado.getCategorias().getCategoriaId().equals(producto.getCategorias().getCategoriaId())){
            categoriasService.eliminarProductoCategoria(productoEncontrado.getProductoId());
            productoEncontrado.setCategorias(producto.getCategorias());
        }
        

        return productosRepository.save(productoEncontrado);
    }

    @Transactional(readOnly = false)
    public ProductosEntity cambiarEstatus(Long productoId, Boolean estatus){
        ProductosEntity producto = this.obtenerProductoPorId(productoId).orElseThrow(()-> new NotFoundException("Producto no encontrado"));
        producto.setEstatus(estatus);
        
        return productosRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public double calcularMargen( Double precio, Double costo){
        Double margen = (precio - costo) / precio * 100;
        return Math.round(margen * 100.0) / 100.0; //2 decimals
    }
}
