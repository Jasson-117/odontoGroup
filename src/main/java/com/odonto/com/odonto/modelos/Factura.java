package com.odonto.com.odonto.modelos;

import lombok.Data;

import java.sql.Date;
@Data

public class Factura {
   int factura_id;
   String prospecto;
   Integer id_cliente;
   String nombreCliente;
   String  nombreVendedor;
    Integer id_vendedor;
    String fecha;
    Double total;
    Double saldo;
     String totalString; // Nueva propiedad para almacenar total formateado
     String saldoString;

}
