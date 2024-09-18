package com.sistem.sistema.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    @Query("SELECT rol FROM RolesEntity rol WHERE rol.nombre!='ROLE_ADMIN'")
    List<RolesEntity> obtenerRoles();

    @Query("SELECT rol FROM RolesEntity rol WHERE rol.nombre =:nombre ")
    Optional<RolesEntity> obtenerRolPorNombre(@Param("nombre") String nombre);

    boolean existsByNombre(String nombre);


}
