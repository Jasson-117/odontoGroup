package com.odonto.com.odonto.dto;

import lombok.Data;

@Data
public class UsuarioDto {
    int documento;
    int id_vendedor;
    String nombre;
    String apellido;
    String contraseña;
    String correo;
}
