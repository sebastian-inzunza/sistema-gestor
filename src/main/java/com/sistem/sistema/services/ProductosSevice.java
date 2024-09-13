package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.ProductosEntity;
import com.sistem.sistema.repository.ProductosRepository;

@Service
public class ProductosSevice {
    
    @Autowired
    ProductosRepository productosRepository;

    @Transactional(readOnly = true)
    public List<ProductosEntity> obtenerProductos(){
        
        return productosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ProductosEntity> obtenerProductoPorId(Long productoId){
        return productosRepository.findProductoById(productoId);
    }

    @Transactional(readOnly = true)
    public boolean isProductExist(String nombre){
        return productosRepository.existsByNombre(nombre);
    }

    @Transactional(readOnly = false)
    public ProductosEntity crearProductos(ProductosEntity producto){
        producto.setFecha(new Timestamp(new Date().getTime()));

        return productosRepository.save(producto);
    }


}
