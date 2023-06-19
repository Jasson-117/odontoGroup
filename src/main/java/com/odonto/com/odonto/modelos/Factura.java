package com.odonto.com.odonto.modelos;

import lombok.Data;

import java.sql.Date;
@Data

public class Factura {
   int factura_id;
   String prospecto;
   int id_cliente;
   String nombreCliente;
   String  nombreVendedor;
    int id_vendedor;
    Date fecha;
    double total;
}
