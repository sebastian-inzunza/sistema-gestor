package com.sistem.sistema.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.repository.EndpointsRepository;

@Service
public class EndpointsService {
    
    @Autowired
    EndpointsRepository endpointsRepository;

    @Transactional(readOnly = true)
    public List<EnpointsEntity> ObtenerEndpoints(){
        return endpointsRepository.findAll();
    }


    @Transactional(readOnly = false)
    public void EliminarEndpointRoles(Long rolId){
        endpointsRepository.eliminarEndpoindsRoles(rolId);
    }
}
