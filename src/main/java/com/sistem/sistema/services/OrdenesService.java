package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.sistem.sistema.models.OrdenProductos;

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

    @Transactional(readOnly = true)
    public String GenerarFolio(){
        String folio = "";
        LocalDate localDate = LocalDate.now();


        Optional<OrdenesEntity> ultimaOrden = ordenesRepository.ObtenerUltimaOrden(String.valueOf(localDate.getYear()));
        if(!ultimaOrden.isEmpty()){
            Integer numero = Integer.valueOf(ultimaOrden.get().getNombre().split("-")[2]);
            folio = localDate.getYear() + "-Orden-" + ++numero;
        }else{
            folio = localDate.getYear() + "-Orden-1";
        }

        return folio;
    }

    @Transactional(readOnly = false)
    public OrdenesEntity CrearOrden (OrdenesEntity orden){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UsuarioEntity usuario = usuarioService.ObtenerUsuarioEmail((String) auth.getPrincipal()).orElseThrow(() -> new NotFoundException("No se encontro id de usuario"));

        orden.setUsuarioId(usuario.getUsuarioId());
        orden.setFecha(new Timestamp(new Date().getTime()));
        orden.setEstatus(OrdenEstatus.ESPERANDO.toString());

        orden.getProductosOrden().forEach(producto ->{
            producto.setPrecio(producto.getCantidad() * producto.getPrecio());
            orden.setTotal(orden.getTotal() + producto.getPrecio()); 
        });

        
        return ordenesRepository.save(orden);
    }

    @Transactional(readOnly = false)
    public void CrearProductosOrdenes(OrdenesEntity orden){

        orden.getProductosOrden().forEach(producto->{
            OrdenesProductosEntity ordenProducto = new OrdenesProductosEntity();

            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setCantidad(producto.getCantidad());
            ordenProducto.setPrecio(producto.getPrecio());

            ordenesProductosRepository.save(ordenProducto);
        });
    }
    
    @Transactional(readOnly = false)
    public void EditarProductosOrdenes(OrdenesEntity orden){


        orden.getProductosOrdenEliminar().forEach(producto ->{
            if (producto.getOrdenProductoId() != null) {  
                OrdenesProductosEntity ordenProducto = ordenesProductosRepository.findById(producto.getOrdenProductoId()).get();              
                ordenesProductosRepository.delete(ordenProducto);
            }
        });

        orden.setTotal(0D);
        orden.getProductosOrden().forEach(producto->{
            OrdenesProductosEntity ordenProducto = new OrdenesProductosEntity();

            orden.setTotal( orden.getTotal() + producto.getCantidad() * producto.getPrecio());

            if (producto.getOrdenProductoId() != null) {  
                ordenProducto = ordenesProductosRepository.findById(producto.getOrdenProductoId()).get();              
            }

            
            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setCantidad(producto.getCantidad());
            ordenProducto.setPrecio(producto.getPrecio());

                    
            ordenesProductosRepository.save(ordenProducto);
            
        });
    
        
        orden.setEstatus(OrdenEstatus.ESPERANDO.toString());
        ordenesRepository.save(orden);
    }

    @Transactional(readOnly = true)
    public boolean OrdenIsExist(String nombre){
        return ordenesRepository.ObtenerOrdenPorNombreEstatus(nombre, OrdenEstatus.CERRADO.toString()).isPresent();
    }

    @Transactional(readOnly = true)
    public List<OrdenesEntity> ObtenerPorEstatus(String estatus){
        return ordenesRepository.ObtenerOrdenEstatus(estatus);
    }

    @Transactional(readOnly = true)
    public Optional<OrdenesEntity> ObtenerPorId(Long id){
        return ordenesRepository.ObtenerPorId(id);
    }

    @Transactional(readOnly = false)
    public void CambiarEstatus(Long ordenId, String estatus){

        OrdenesEntity orden = ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        if(estatus.equals(OrdenEstatus.LISTO.toString())){
            List<OrdenesProductosEntity> productos = ordenesProductosRepository.obtenerInformacion(orden.getOrdenId());
            
            productos.forEach(producto ->{
                producto.setAtendido(true);
                ordenesProductosRepository.save(producto);
            });
        }
        
        orden.setEstatus(estatus);
        ordenesRepository.save(orden);

    }

    @Transactional(readOnly = false)
    public List<OrdenesEntity> ObtenerOrdenes(){
        List<OrdenesEntity> ordenes = ordenesRepository.findAll();
        
        ordenes.forEach(orden -> {
            List<OrdenProductos> productos = ordenesProductosRepository.obtenerProductos(orden.getOrdenId());
            orden.setProductosOrden(productos);  
        });

        return ordenes;
    }
}

