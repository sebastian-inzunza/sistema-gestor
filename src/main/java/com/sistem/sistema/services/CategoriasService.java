package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.CategoriasEntity;
import com.sistem.sistema.repository.CategoriasRepository;

@Service
public class CategoriasService {
    
    @Autowired 
    CategoriasRepository categoriasRepository;

    @Transactional(readOnly = true)
    public List<CategoriasEntity> obtenerCategorias(){
        return categoriasRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CategoriasEntity> obtenerCategoriaPorId(Long id){
        return categoriasRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public CategoriasEntity crearCategoria(CategoriasEntity categoriasEntity){
        categoriasEntity.setFecha(new Timestamp(new Date().getTime()));
        categoriasEntity.setEstatus(true);
        return categoriasRepository.save(categoriasEntity);
    }

    @Transactional(readOnly = false)
    public CategoriasEntity editarCategoria(CategoriasEntity categoriasEntity){
        return categoriasRepository.save(categoriasEntity);
    }

    @Transactional(readOnly = true)
    public boolean isExistcategoria(String nombre){
        return categoriasRepository.existsByNombre(nombre);
    }

    @Transactional(readOnly = false)
    public void eliminarProductoCategoria(Long productoId){
        categoriasRepository.eliminarProductoCategoria(productoId);
    }
}
