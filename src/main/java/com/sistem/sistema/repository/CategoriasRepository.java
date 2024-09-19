package com.sistem.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.CategoriasEntity;

import jakarta.transaction.Transactional;

public interface CategoriasRepository extends JpaRepository<CategoriasEntity, Long> {

    @Modifying
    @Transactional  
    @Query(value = "DELETE FROM productos_categorias WHERE productoId=:productoId", nativeQuery = true)
    void eliminarProductoCategoria(@Param("productoId") Long productoId);

    boolean existsByNombre(String nombre);

}
