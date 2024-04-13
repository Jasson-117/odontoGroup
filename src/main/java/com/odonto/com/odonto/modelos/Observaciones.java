package com.odonto.com.odonto.modelos;

import lombok.Data;

@Data
public class Observaciones {
    int idObservacion;
    String observacion;
    int idCliente;
    String fecha;
}
