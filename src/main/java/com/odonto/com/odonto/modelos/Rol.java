package com.odonto.com.odonto.modelos;

import lombok.Data;

@Data
public class Rol {
    int id_rol;
    String nombre;

    public Rol(String roleUser) {
        super();
        this.nombre = roleUser;
    }
}
