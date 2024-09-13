package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistem.sistema.entity.CategoriasEntity;
import com.sistem.sistema.repository.CategoriasRepository;

@Service
public class CategoriasService {
    
    @Autowired 
    CategoriasRepository categoriasRepository;

    public List<CategoriasEntity> obtenerCategorias(){
        return categoriasRepository.findAll();
    }

    public CategoriasEntity crearCategoria(CategoriasEntity categoriasEntity){
        categoriasEntity.setFecha(new Timestamp(new Date().getTime()));
        categoriasEntity.setEstatus(true);
        return categoriasRepository.save(categoriasEntity);
    }

    public boolean isExistcategoria(String nombre){
        return categoriasRepository.existsByNombre(nombre);
    }
}
