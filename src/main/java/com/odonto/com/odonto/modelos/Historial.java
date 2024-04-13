package com.odonto.com.odonto.modelos;

import lombok.Data;

import java.util.Date;

@Data
public class Historial {
    int facturaId;
    int idCliente;
    String fecha;
    String tipoIdentificacion;
    String motivoDeConsulta;
    String origenDeLaEnfermedad;
    String historiaDeEnfermedadActual;
    String antecedentesDeImportancia;
    String observaciones;
    String impresionDiagnostica;
    String valoracionEspecialista;
    byte[] imagen;

}
