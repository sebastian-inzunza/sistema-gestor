package com.sistem.sistema.models;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDTO {
    
    private String nombre;
    private Long usuarioId;
    private String estatus;
    private Double total;
    private Boolean llevar;
    private Timestamp fecha;

}