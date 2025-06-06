package com.sistem.sistema.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.sql.Date;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

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

    @Autowired
    EntityManager BDConnection;

    OrderWebSocketHandler orderWebSocketHandler;

    OrdenEstatus ordenEstatus;

    static final String SQL ="SELECT * FROM ordenes <WHERE>";

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

        this.ProductsExist(orden); // Validate if all product exist

        UsuarioEntity usuario = usuarioService.ObtenerUsuarioEmail((String) auth.getPrincipal()).orElseThrow(() -> new NotFoundException("No se encontro id de usuario"));

        orden.setTotal(0D);
        orden.setUsuarioId(usuario.getUsuarioId());
        orden.setFecha(new Timestamp(new java.util.Date().getTime()));
        orden.setEstatus(OrdenEstatus.ESPERANDO.toString());
        
        return ordenesRepository.save(orden);
    }

    @Transactional(readOnly = true)
    public void ProductsExist(OrdenesEntity order){
        order.getProductosOrden().forEach(ordenProducto -> {
            //if product is not found
            if(!productosSevice.isProductExist(ordenProducto.getProductoId())){
                throw new NotFoundException("Producto no encontrado");
            }
        });
    }

    @Transactional(readOnly = false)
    public void CrearProductosOrdenes(OrdenesEntity orden) throws Exception{

        orden.getProductosOrden().forEach(ordenProducto->{
            ProductosEntity producto = productosSevice.obtenerProductoPorId(ordenProducto.getProductoId()).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
            
            ordenProducto.setOrdenId(orden.getOrdenId());
            ordenProducto.setProductoId(producto.getProductoId());
            ordenProducto.setPrecio(producto.getPrecio());
            ordenProducto.setCosto(producto.getCosto());
            ordenProducto.setMargen(productosSevice.calcularMargen(producto.getPrecio(), producto.getCosto()));
            ordenProducto.setProducto(producto.getNombre());
            ordenProducto.setAtendido(false); //Revisar aca
            ordenProducto.setPreparado(producto.getPreparado());

            ordenesProductosRepository.save(ordenProducto);
        });

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
        Optional<OrdenesEntity> orden = ordenesRepository.ObtenerPorId(id);
        if(orden.isPresent())
            orden.get().setProductosOrden(ordenesProductosRepository.obtenerProductos(id));
        
        return orden; 
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

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<OrdenesEntity> ObtenerOrdenesPorDiaDinamic(Date date, Long userId, String status ){
        String WHERE_SENTENCE = " WHERE CAST(fecha as DATE) = '"+ date + "' ";

        WHERE_SENTENCE = (userId != null && userId != 0)? WHERE_SENTENCE + " AND usuario_id = " + userId : WHERE_SENTENCE;
        WHERE_SENTENCE = (status != null && !status.equals(""))? WHERE_SENTENCE + " AND estatus = '" + status + "'" : WHERE_SENTENCE;

        System.out.println(WHERE_SENTENCE);
        String SQL_FINAL = SQL.replace("<WHERE>", WHERE_SENTENCE);

        Query result = BDConnection.createNativeQuery(SQL_FINAL, OrdenesEntity.class);
        List<OrdenesEntity> ordenes = result.getResultList();

        return ordenes;

        

    }

    @Transactional(readOnly = true)
    public boolean areProductosPreparados(Long orderId){
        return ordenesProductosRepository.obtenerInformacionPreparado(orderId).isEmpty();
    }
}

