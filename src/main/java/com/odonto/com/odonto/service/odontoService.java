package com.odonto.com.odonto.service;

import com.odonto.com.odonto.modelos.*;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.List;

public interface odontoService extends UserDetailsService {
    public List<Cliente> findAllClientes();
    public List<Cliente> findAllsCliente();

    Double calcularSaldo(int documento);

    List<Cliente> findAllCliente(int documento);

    List<Cliente> findAllClienteByNombre(String nombre);

    public int saveCliente(Cliente cliente);
    public int updateCliente(Cliente cliente);
    public int deleteCliente(int id);

    //============================
    public List<Factura> findAllFacturas();
    public List<Factura> findAllsFactura();

    List<Factura> findAllFactura(int id_factura);

    public int saveFactura(Factura factura);
    public int updateFactura(Factura factura);
    public int deleteFactura(int id);

    Factura findAllFacturaByFecha(String fecha);

    Factura findAllFacturaByFechaHora(String fecha);

    Factura findAllFacturaByFecha2(String fecha);

    //=================================================0
    List<Usuario> findAllUsuario(int documento);
    public int updateUsuario(Usuario usuario);


    int saveUsuario(Usuario usuario);

    Usuario findAllUsuarioByPassword(String documento);

    Usuario findAllUsuarioByEmail(String documento);

    List<Factura> findAllFacturaByFecha(String fecha1, String fecha2);

    int deleteUsuario(int id);

    List<Usuario> findAllUsuario2s();

    List<Historial> findAllHistorial(int documento);



    int updateHistorial(Historial historial);

    int saveHistorial(Historial historial);

    List<Observaciones> findAllObservaciones(int documento);

    int saveObservacion(Observaciones observaciones);

    int deleteObservacion(int id);

    Usuario findUserByEmail(String documento);

    List<Factura> findAllFacturasByDocumento(int documento);

    List<byte[]> obtenerImagenPorId(int id);
}
