package com.sistem.sistema.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.OrdenesProductosEntity;
import com.sistem.sistema.models.OrdenProductos;

public interface OrdenesProductosRepository extends  JpaRepository<OrdenesProductosEntity, Long> {
    @Query("SELECT new com.sistem.sistema.models.OrdenProductos(pro.productoId, pro.nombre, ord.cantidad, pro.precio) FROM OrdenesProductosEntity ord LEFT JOIN ProductosEntity pro ON ord.productoId=pro.productoId WHERE ord.ordenId=:ordenId")
    List<OrdenProductos> obtenerProductos(@Param("ordenId") Long ordenId);
} 
