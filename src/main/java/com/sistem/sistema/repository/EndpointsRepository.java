package com.sistem.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistem.sistema.entity.EnpointsEntity;

import jakarta.transaction.Transactional;

public interface EndpointsRepository extends JpaRepository<EnpointsEntity, Long>  {
    
    @Modifying
    @Transactional  
    @Query(value = "DELETE FROM enpoints_roles WHERE rolId=:rolId", nativeQuery = true)
    void eliminarEndpoindsRoles(@Param("rolId") Long rolId);
}
