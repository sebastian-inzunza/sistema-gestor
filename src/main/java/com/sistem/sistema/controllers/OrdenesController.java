package com.sistem.sistema.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.OrdenEstatus;
import com.sistem.sistema.services.OrdenesService;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/ordenes")
public class OrdenesController {
    
    @Autowired
    OrdenesService ordenesService;
    
    OrdenEstatus ordenEstatus;

    @GetMapping("")
    public List<OrdenesEntity> ObtenerOrdenes() {
        return ordenesService.ObtenerOrdenes();

    }

    @PostMapping("crear")
    public ResponseEntity<Object> CrearOrden(@RequestBody OrdenesEntity orden) {

        if(orden.getProductosOrden()== null || orden.getProductosOrden().isEmpty()){
            throw new NotFoundException("Para crear una orden debe tener por lo menos un producto asignado");
        }
        
        if(ordenesService.OrdenIsExist(orden.getNombre())){
            throw new NotFoundException(String.format("ya existe una orden activa con el nombre '%s', ingresa otro nombre", orden.getNombre()));
        }
        


        OrdenesEntity ordenCreada = ordenesService.CrearOrden(orden); 
        ordenesService.CrearProductosOrdenes(ordenCreada);

        return ResponseEntity.ok().body("Orden creada");
    }
    
    @SuppressWarnings("static-access")
    @PutMapping("/lista")
    public ResponseEntity<Object> CambiarEstatusListo( @RequestParam(required = true) Long ordenId) throws Exception {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        if(ordene.getLlevar()){
            ordenesService.CambiarEstatus(ordene.getOrdenId(), ordenEstatus.CERRADO.toString());
        }else{
            ordenesService.CambiarEstatus(ordene.getOrdenId(), ordenEstatus.LISTO.toString());
        }
        return ResponseEntity.ok().body("Orden Lista");
    }

    @SuppressWarnings("static-access")
    @PutMapping("/cerrar")
    public ResponseEntity<Object> CerrarOrden( @RequestParam(required = true) Long ordenId) {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        ordenesService.CambiarEstatus(ordene.getOrdenId(), ordenEstatus.CERRADO.toString());

        return ResponseEntity.ok().body("Orden Cerrada");
    }

}
