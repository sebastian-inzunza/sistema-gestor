package com.sistem.sistema.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.RolesEntity;

import jakarta.transaction.Transactional;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {

    @Query("SELECT rol FROM RolesEntity rol WHERE rol.nombre!='ROLE_ADMIN'")
    List<RolesEntity> obtenerRoles();

    @Query("SELECT rol FROM RolesEntity rol WHERE rol.nombre =:nombre ")
    Optional<RolesEntity> obtenerRolPorNombre(@Param("nombre") String nombre);

    @Modifying
    @Transactional  
    @Query(value = "DELETE FROM usuarios_roles WHERE usuarioId=:usuarioId", nativeQuery = true)
    void eliminarUsuariosRoles(@Param("usuarioId") Long usuarioId);

    boolean existsByNombre(String nombre);


}
