package com.sistem.sistema.entity;

import java.sql.Timestamp;
import java.util.List;

import com.sistem.sistema.models.OrdenProductos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordenes")
public class OrdenesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orden_id")
    private Long ordenId;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Basic
    @Column(name = "estatus")
    private String estatus;

    @Basic
    @Column(name = "total")
    private double total;

    @Basic
    @Column(name = "llevar")
    private Boolean llevar;

    @Basic
    @Column(name = "fecha")
    private Timestamp fecha;

    @Transient
    private List<OrdenProductos> productosOrden;

    @Transient
    private List<OrdenProductos> productosOrdenEliminar;

}
