package com.sistem.sistema.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "endpoints")
@NoArgsConstructor
@AllArgsConstructor
public class EnpointsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endpointId")
    private Long endpointId;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "method")
    private String method;

    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    
    @JsonIgnoreProperties({"endpoints",  "handler", "hibernateLazyInitializer"})
    @ManyToMany(mappedBy = "endpoints", fetch = FetchType.EAGER)
    List<RolesEntity> roles;
}
