package com.odonto.com.odonto.modelos;

import lombok.Data;

import java.util.Date;

@Data
public class Cliente {
    int documento;
    String nombre;
    String apellido;
    String correo;
    String direccion;
    String telefono;


}
