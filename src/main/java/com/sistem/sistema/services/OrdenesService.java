package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.entity.OrdenesProductosEntity;
import com.sistem.sistema.entity.UsuarioEntity;
import com.sistem.sistema.repository.OrdenesProductosRepository;
import com.sistem.sistema.repository.OrdenesRepository;

import com.sistem.sistema.exception.NotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenesService {
    
    @Autowired
    OrdenesRepository ordenesRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    OrdenesProductosRepository ordenesProductosRepository;

    OrdenEstatus ordenEstatus;

    @Transactional(readOnly = false)
    public OrdenesEntity CrearOrden (OrdenesEntity orden){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UsuarioEntity usuario = usuarioService.ObtenerUsuarioEmail((String) auth.getPrincipal()).orElseThrow(() -> new NotFoundException("No se encontro id de usuario"));

        orden.setUsuarioId(usuario.getUsuarioId());
        orden.setFecha(new Timestamp(new Date().getTime()));
        orden.setEstatus(OrdenEstatus.EN_ESPERA.toString());
        return ordenesRepository.save(orden);
    }

    @Transactional(readOnly = false)
    public void CrearProductosOrdenes(OrdenesEntity orden){
        orden.getProductosOrden().forEach(producto->{
            OrdenesProductosEntity ordenProducto = new OrdenesProductosEntity();

            System.out.println(producto.toString());
            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setCantidad(producto.getCantidad());

            ordenesProductosRepository.save(ordenProducto);
        });
    }

    public boolean OrdenIsExist(String nombre){
        return ordenesRepository.ObtenerOrdenPorNombreEstatus(nombre, OrdenEstatus.CERRADO.toString()).isPresent();
    }
}
