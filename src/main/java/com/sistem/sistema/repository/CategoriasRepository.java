package com.sistem.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistem.sistema.entity.CategoriasEntity;

public interface CategoriasRepository extends JpaRepository<CategoriasEntity, Long> {

    boolean existsByNombre(String nombre);

}
