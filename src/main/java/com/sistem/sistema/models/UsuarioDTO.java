package com.sistem.sistema.models;

import java.sql.Timestamp;

public class UsuarioDTO {
    private Long usuarioId;
    private String nombre;
    private String email;
    private Boolean estatus;
    private Timestamp fecha;


    public UsuarioDTO() {
    }

    public UsuarioDTO(Long usuarioId, String nombre, String email,  Boolean estatus, Timestamp fecha) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
 
        this.estatus = estatus;
        this.fecha = fecha;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
    public String getNombre() {
        return nombre;
    }
    public String getEmail() {
        return email;
    }
    public Boolean getEstatus() {
        return estatus;
    }
    public Timestamp getFecha() {
        return fecha;
    }

    
}
