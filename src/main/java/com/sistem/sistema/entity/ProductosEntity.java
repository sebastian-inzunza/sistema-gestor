package com.sistem.sistema.entity;


import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long productoId;

    @Basic
    @Column(name = "nombre", unique = true)
    private String nombre;

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
    @ColumnDefault("0.0")
    private Double margen;

    @Basic
    @Column(name = "descripcion")
    private String descripcion;

    @Basic
    @Column(name = "imagen")
    private String imagen;

    @Basic
    @Column(name = "estatus")
    private Boolean estatus;

    @Basic
    @Column(name = "fecha")
    private Timestamp fecha;

    @Basic
    @Column(name = "preparado")
    @ColumnDefault("false")
    private Boolean preparado;

    @JsonIgnoreProperties({"productos", "handler", "hibernateLazyInitializer"})
    @ManyToOne()
    @JoinTable(
        name = "productos_categorias",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"producto_id", "categoria_id"})}
    )
    private CategoriasEntity categorias;

}
