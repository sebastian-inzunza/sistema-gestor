package com.sistem.sistema.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.OrdenesEntity;

public interface OrdenesRepository extends JpaRepository<OrdenesEntity, Long> {
    

    @Query("SELECT ord FROM OrdenesEntity ord WHERE ord.nombre=:nombre AND ord.estatus!=:estatus")
    Optional<OrdenesEntity> ObtenerOrdenPorNombreEstatus(@Param("nombre") String nombre, @Param("estatus") String estatus);

    @Query("SELECT ord FROM OrdenesEntity ord WHERE ord.estatus=:estatus")
    List<OrdenesEntity> ObtenerOrdenEstatus(@Param("estatus") String estatus);

    @Query("SELECT ord FROM OrdenesEntity ord WHERE  ord.ordenId=:id")
    Optional<OrdenesEntity> ObtenerPorId(@Param("id") Long id);

    @Query(value = "SELECT * FROM public.ordenes WHERE orden_id = (SELECT MAX(orden_id) FROM public.ordenes) AND fecha BETWEEN :fechaInicio AND :fechaFin", nativeQuery = true)
    Optional<OrdenesEntity> ObtenerUltimaOrden(@Param("fechaInicio") Timestamp fechaInicio, @Param("fechaFin") Timestamp fechaFin);

}
