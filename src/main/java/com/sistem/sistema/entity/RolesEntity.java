package com.sistem.sistema.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RolesEntity {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "rolId")
    private Long rolId;

    @Basic
    @Column(name = "nombre", unique = true)
    private String nombre;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnoreProperties({"roles",  "handler", "hibernateLazyInitializer"})
    @ManyToMany(mappedBy = "roles")
    List<UsuarioEntity> usuarios;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnoreProperties({"roles", "handler", "hibernateLazyInitializer"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "enpoints_roles",
        joinColumns = @JoinColumn(name = "rolId"),
        inverseJoinColumns = @JoinColumn( name = "endpointId"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"endpointId", "rolId"})}
    )
    private List<EnpointsEntity> endpoints;


}
