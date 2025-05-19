package com.sistem.sistema.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.exception.NotFoundException;
import com.sistem.sistema.services.OrdenEstatus;
import com.sistem.sistema.services.OrdenesService;
import com.sistem.sistema.socket.OrderWebSocketHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api/ordenes")
public class OrdenesController {
    
    @Autowired
    OrdenesService ordenesService;

    @Autowired
    OrderWebSocketHandler orderWebSocketHandler;

    @GetMapping("")
    public List<OrdenesEntity> ObtenerOrdenes() {
        return ordenesService.ObtenerOrdenes();
    }

    @GetMapping("paginado")
    public ResponseEntity<Object> ObtenerOrdenesPaginado(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer limit,
        @RequestParam(required = false) String estatus,
        @RequestParam(required =  false) Integer userId
    ) {

        OrdenesEntity orden = new OrdenesEntity();
        
        if(userId != null && userId >0){
            orden.setUsuarioId((long) userId);
        }

        if(estatus != null && !estatus.equals("")){
            orden.setEstatus(estatus);
        }

        Page<OrdenesEntity> ordenesPaged = ordenesService.ObtenerOrdenesPaginado(orden, page, limit);
        
        return ResponseEntity.ok(ordenesPaged);
    }
    

    @GetMapping("/dia")
    public ResponseEntity<Object> ObtenerOrdenesPorDia(
            @RequestParam(required = true) Date date,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status
        ) {
        try {
            return ResponseEntity.ok().body(ordenesService.ObtenerOrdenesPorDiaDinamic(date, userId, status));
        } catch (Exception e) {
            throw new NotFoundException("Error al obtener ordenes en api/ordenes/dia");
        }
    }
    

    @PostMapping("crear")
    public ResponseEntity<Object> CrearOrden(@RequestBody OrdenesEntity orden) throws Exception {

        if(orden.getProductosOrden()== null || orden.getProductosOrden().isEmpty()){
            throw new NotFoundException("Para crear una orden debe tener por lo menos un producto asignado");
        }

        orden.setNombre(ordenesService.GenerarFolio());

        if(ordenesService.OrdenIsExist(orden.getNombre())){
            throw new NotFoundException(String.format("ya existe una orden activa con el nombre '%s', ingresa otro nombre", orden.getNombre()));
        }
        

        
        OrdenesEntity ordenCreada = ordenesService.CrearOrden(orden); 
        ordenesService.CrearProductosOrdenes(ordenCreada);
        
        
        return ResponseEntity.ok().body("Orden creada con folio " + orden.getNombre());
    }
    
    @PutMapping("agregar/productos/{ordenId}")
    public ResponseEntity<Object> AgregarProductos(@PathVariable Long ordenId, @RequestBody OrdenesEntity orden) {
        OrdenesEntity ordenEncontrada = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("Orden no encontrada"));

        if(ordenEncontrada.getEstatus().equals(OrdenEstatus.CERRADO.toString())){
            throw new NotFoundException("La orden ya fue cerrada y no se puede agregar y/o eliminar productos");
        }

        ordenEncontrada.setProductosOrden(orden.getProductosOrden());
        ordenEncontrada.setProductosOrdenEliminar(orden.getProductosOrdenEliminar());


        try {
            ordenesService.EditarProductosOrdenes(ordenEncontrada);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.ok().body("Productos Agregados");
    }

    @PutMapping("/preparando")
    public ResponseEntity<Object> OrdenPreparando( @RequestParam(required = true) Long ordenId) throws Exception {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        //Avisamos a todos los los que esten conectados al socket que una orden fue tomada
        orderWebSocketHandler.notificarCambioEstatusOrdenes(String.valueOf(ordene.getOrdenId()), OrdenEstatus.PREPARANDO.toString());    
        
        return ResponseEntity.ok().body("Orden Prerandonse");
    }

    @PutMapping("/lista")
    public ResponseEntity<Object> OrdenLista( @RequestParam(required = true) Long ordenId) throws Exception {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        if(!ordene.getEstatus().equals(OrdenEstatus.PREPARANDO.toString())){
            throw new NotFoundException(String.format("La orden esta en estatus '%s', no se puede cambiar orden a lista", ordene.getEstatus()));
        }

        ordenesService.CambiarEstatus(ordene.getOrdenId(), OrdenEstatus.LISTO.toString());

        if(ordene.getLlevar()){
            ordenesService.CambiarEstatus(ordene.getOrdenId(), OrdenEstatus.CERRADO.toString());
        }

        return ResponseEntity.ok().body("Orden Lista");
    }

    @PutMapping("/cerrar")
    public ResponseEntity<Object> OrdenCerrada( @RequestParam(required = true) Long ordenId) {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        ordenesService.CambiarEstatus(ordene.getOrdenId(), OrdenEstatus.CERRADO.toString());

        return ResponseEntity.ok().body("Orden Cerrada");
    }

    @PutMapping("/cancelar")
    public ResponseEntity<Object> OrdenCancelada( @RequestParam(required = true) Long ordenId) {
        OrdenesEntity ordene = ordenesService.ObtenerPorId(ordenId).orElseThrow(()-> new NotFoundException("No se encontro la orden"));

        ordenesService.CambiarEstatus(ordene.getOrdenId(), OrdenEstatus.CANCELADO.toString());

        return ResponseEntity.ok().body("Orden Cancelada");
    }

}
