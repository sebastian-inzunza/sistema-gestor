package com.sistem.sistema.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordenes_productos")
public class OrdenesProductosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Basic
    @Column(name = "orden_id")
    private Long ordenId;

    @Basic
    @Column(name = "producto_id")
    private Long productoId;

    @Basic
    @Column(name = "cantidad")
    @ColumnDefault("0.0")
    private Integer cantidad;

    @Basic
    @Column(name = "precio")
    @ColumnDefault("0.0")
    private Double precio;
    
    @Basic
    @Column(name = "costo")
    @ColumnDefault("0.0")
    private Double costo;
    
    @Basic
    @Column(name = "margen")
    private Double margen;
    
    @Basic
    @Column(name = "atendido")
    private boolean atendido;

    @Basic
    @Column(name = "producto")
    private String producto;

    @Basic
    @Column(name = "preparado")    
    private Boolean preparado;
}
