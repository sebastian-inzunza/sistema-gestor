package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistem.sistema.entity.OrdenesEntity;
import com.sistem.sistema.entity.OrdenesProductosEntity;
import com.sistem.sistema.entity.ProductosEntity;
import com.sistem.sistema.entity.UsuarioEntity;
import com.sistem.sistema.repository.OrdenesProductosRepository;
import com.sistem.sistema.repository.OrdenesRepository;
import com.sistem.sistema.socket.OrderWebSocketHandler;
import com.sistem.sistema.exception.NotFoundException;


@Service
public class OrdenesService {
    
    @Autowired
    OrdenesRepository ordenesRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    OrdenesProductosRepository ordenesProductosRepository;

    @Autowired 
    ProductosSevice productosSevice;

    
    OrderWebSocketHandler orderWebSocketHandler;

    OrdenEstatus ordenEstatus;

    @Transactional(readOnly = true)
    public String GenerarFolio() throws ParseException{

        String folio = "";
        LocalDate localDate = LocalDate.now();

        String date = localDate.getYear() + "-01-01 00:00:00";
        Timestamp timestampInicio = Timestamp.valueOf(date);

        date = localDate.getYear() + "-12-31 23:59:59";
        Timestamp timestampFin = Timestamp.valueOf(date);

        Optional<OrdenesEntity> ultimaOrden = ordenesRepository.ObtenerUltimaOrden(timestampInicio, timestampFin);

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
        orden.setTotal(0D);
        orden.setUsuarioId(usuario.getUsuarioId());
        orden.setFecha(new Timestamp(new Date().getTime()));
        orden.setEstatus(OrdenEstatus.ESPERANDO.toString());
        
        return ordenesRepository.save(orden);
    }

    @Transactional(readOnly = false)
    public void CrearProductosOrdenes(OrdenesEntity orden) throws Exception{

        orden.getProductosOrden().forEach(ordenProducto->{
            ProductosEntity producto = productosSevice.obtenerProductoPorId(ordenProducto.getProductoId()).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

            

            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setPrecio(producto.getPrecio());
            ordenProducto.setProducto(producto.getNombre());
            ordenProducto.setAtendido(false);
            ordenProducto.setPreparado(producto.getPreparado());

            ordenesProductosRepository.save(ordenProducto);
        });

        List<OrdenesProductosEntity> isPreparados = ordenesProductosRepository.obtenerInformacionPreparado(orden.getOrdenId());

        if(isPreparados.isEmpty()){
            //Si la orden tiene todos sus productos como previamente preparado (Agua, refrescos, postres) se manda direcatmante a orden lista 
            this.CambiarEstatus(orden, OrdenEstatus.LISTO.toString());//Cambia a Listo y calcula total

            if(orden.getLlevar()){ //Si la orden es para llevar automaticamente se cierra despues de estar lista
                this.CambiarEstatus(orden, OrdenEstatus.CERRADO.toString());
            }

        }else{
            orderWebSocketHandler.notificarOrdenCreada();  //Si hay productos que son necesarios preararse en cocina, notifica una nueva orden en espera
        }

    }
    
    
    @Transactional(readOnly = false)
    public void EditarProductosOrdenes(OrdenesEntity orden) throws Exception{


        //Elmina a los productos
        orden.getProductosOrdenEliminar().forEach(producto ->{
            if (!producto.isAtendido()) {  
                OrdenesProductosEntity ordenProducto = ordenesProductosRepository.findById(producto.getId()).get();              
                ordenesProductosRepository.delete(ordenProducto);
            }
        });

        orden.setTotal(0D);
        orden.getProductosOrden().forEach(ordenProducto->{
            ProductosEntity producto = productosSevice.obtenerProductoPorId(ordenProducto.getProductoId()).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

            if(ordenProducto.getId() != null){
                ordenProducto.setAtendido(false);
            }


            ordenProducto.setPrecio(producto.getPrecio());
            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setProducto(producto.getNombre());
            ordenProducto.setPreparado(producto.getPreparado());
            
            ordenesProductosRepository.save(ordenProducto);
            
        });
    
        List<OrdenesProductosEntity> isPreparados = ordenesProductosRepository.obtenerInformacionPreparado(orden.getOrdenId());
        //Todos los productos que se agregaron no es necesario mandar a preparacion 
        if(isPreparados.isEmpty()){
            this.CambiarEstatus(orden.getOrdenId(), OrdenEstatus.LISTO.toString());
        }else{
            orden.setEstatus(OrdenEstatus.ESPERANDO.toString());
            ordenesRepository.save(orden);
            orderWebSocketHandler.notificarOrdenCreada();  //Si hay productos que son necesarios preararse en cocina, notifica una nueva orden en espera

        }

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
                orden.setTotal(orden.getTotal() + (producto.getPrecio() *  producto.getCantidad()));

                producto.setAtendido(true);
                ordenesProductosRepository.save(producto);
            });
            
        }
        
        orden.setEstatus(estatus);
        ordenesRepository.save(orden);
    }

    
    @Transactional(readOnly = false)
    public void CambiarEstatus( OrdenesEntity orden, String estatus){

        if(estatus.equals(OrdenEstatus.LISTO.toString())){
            List<OrdenesProductosEntity> productos = ordenesProductosRepository.obtenerInformacion(orden.getOrdenId());
            
            productos.forEach(producto ->{
                orden.setTotal(orden.getTotal() + (producto.getPrecio() *  producto.getCantidad()));

                producto.setAtendido(true);
                ordenesProductosRepository.save(producto);
            });
            
        }
        
        orden.setEstatus(estatus);
        ordenesRepository.save(orden);

    }

    @Transactional(readOnly = true)
    public List<OrdenesEntity> ObtenerOrdenes(){
        List<OrdenesEntity> ordenes = ordenesRepository.findAll();
        
        ordenes.forEach(orden -> {
            List<OrdenesProductosEntity> productos = ordenesProductosRepository.obtenerProductos(orden.getOrdenId());
            orden.setProductosOrden(productos);  
        });

        return ordenes;
    }

    @Transactional(readOnly= true)
    public Page<OrdenesEntity> ObtenerOrdenesPaginado(OrdenesEntity ordenSearch, Integer page, Integer limit){

        
        ExampleMatcher matcher = ExampleMatcher.matching()
                                .withMatcher("total", new GenericPropertyMatcher().ignoreCase())
                                .withMatcher("estatus", new GenericPropertyMatcher().exact())
                                .withMatcher("usuarioId", new GenericPropertyMatcher().exact());

        Page<OrdenesEntity> ordenes = ordenesRepository.findAll(Example.of(ordenSearch, matcher),PageRequest.of(page, limit));

        ordenes.forEach( orden ->{
            List<OrdenesProductosEntity> productos = ordenesProductosRepository.obtenerProductos(orden.getOrdenId());
            orden.setProductosOrden(productos); 
        });
        return ordenes;
    }
}

