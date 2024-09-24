package com.sistem.sistema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.OrdenesEntity;

public interface OrdenesRepository extends JpaRepository<OrdenesEntity, Long> {
    

    @Query("SELECT ord FROM OrdenesEntity ord WHERE ord.nombre=:nombre AND ord.estatus!=:estatus")
    Optional<OrdenesEntity> ObtenerOrdenPorNombreEstatus(@Param("nombre") String nombre, @Param("estatus") String estatus);
}
