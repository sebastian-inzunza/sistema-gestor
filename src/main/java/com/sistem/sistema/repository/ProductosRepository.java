package com.sistem.sistema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.ProductosEntity;

public interface ProductosRepository extends JpaRepository<ProductosEntity, Long>{

    @Query("SELECT pro FROM ProductosEntity pro WHERE pro.productoId =:productoId")
    Optional<ProductosEntity> findProductoById(@Param("productoId") Long productoId);

    boolean existsByNombre(String nombre);


} 
