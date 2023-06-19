package com.odonto.com.odonto.modelos;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Data
public class Usuario {
   int documento;
    int id_vendedor;
   String nombre;
   String apellido;
   String contraseña;
   String correo;
   Collection<Rol> roles;
   public Usuario() {
      // Constructor predeterminado vacío
   }

   public Usuario(int documento, int idVendedor, String nombre, String apellido, String contraseña, String correo, List<Rol> roleUser) {
   this.documento = documento;
   this.id_vendedor = idVendedor;
   this.nombre = nombre;
   this.apellido = apellido;
   this.contraseña = contraseña;
   this.correo = correo;
   this.roles = roleUser;
   }
   public Usuario(int documento, int idVendedor, String nombre, String apellido, String contraseña, String correo){
      this.documento = documento;
      this.id_vendedor = idVendedor;
      this.nombre = nombre;
      this.apellido = apellido;
      this.contraseña = contraseña;
      this.correo = correo;
   }
}
