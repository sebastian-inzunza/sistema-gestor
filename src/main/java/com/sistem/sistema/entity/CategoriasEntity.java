package com.sistem.sistema.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="categorias", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class CategoriasEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Long categoriaId;

    @Basic
    @Column(name = "nombre")
    private String nombre;

    @Basic
    @Column(name = "estatus")
    private Boolean estatus;

    @Basic
    @Column(name = "fecha")
    private Timestamp fecha;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnoreProperties({"categorias",  "handler", "hibernateLazyInitializer"})
    @OneToMany(mappedBy = "categorias")
    List<ProductosEntity> productos;
}

/*
 * [{ 
 *      id:1,
 *      comment: "HOLA"
 *      tags[]: ["Java", "c++"]
 * },
 * { 
 *      id:2,
 *      comment: "HOLA"
 *      tags[]: ["python", "c++"]
 * }]

[
    {
        id: "DAVO"
        tags[]:["Java", "c++"]
    },
    {
        id: "YOVA"
        tags[]:["python", "c++"]
    }
]

[
    [1, "DAVO"],
    [2, "YOVA]
]

 */
