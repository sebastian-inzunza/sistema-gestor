package com.sistem.sistema.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("SELECT usr FROM UsuarioEntity usr WHERE usr.estatus = true")
    List<UsuarioEntity> obtenerUsuarios();

    @Query("SELECT usr FROM UsuarioEntity usr WHERE usr.usuarioId=:id")
    Optional<UsuarioEntity> obtenerUsuarioPorId(@Param("id") Long id);

    // @Query("SELECT new com.sistem.sistema.models.UsuarioDTO( usr.usuarioId, usr.nombre, usr.email, usr.estatus, usr.fecha) FROM UsuarioEntity usr WHERE usr.email=:email")
    // Optional<UsuarioDTO> obtenerUsuarioPorEmail(@Param("email") String email);

    @Query("SELECT usr FROM UsuarioEntity usr WHERE usr.email=:email")
    Optional<UsuarioEntity> obtenerUsuarioPorEmail(@Param("email") String email);

    boolean existsByEmail(String email);
} 