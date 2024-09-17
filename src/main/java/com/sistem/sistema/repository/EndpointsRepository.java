package com.sistem.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistem.sistema.entity.EnpointsEntity;

public interface EndpointsRepository extends JpaRepository<EnpointsEntity, Long>  {
    
}
