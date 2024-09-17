package com.sistem.sistema.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.sistema.entity.EnpointsEntity;
import com.sistem.sistema.repository.EndpointsRepository;

@Service
public class EndpointsService {
    
    @Autowired
    EndpointsRepository endpointsRepository;

    public List<EnpointsEntity> ObtenerEndpoints(){
        return endpointsRepository.findAll();
    }
}
