package com.odonto.com.odonto.repository;

import com.odonto.com.odonto.modelos.*;

import java.util.List;

public interface odontoRepository {
    List<Cliente> findAllClientes();

    public List <Cliente> findAllCliente(int documento);

    List<Cliente> findAllClienteByNombre(String nombre);

    public int saveCliente(Cliente cliente);
    public int updateCliente(Cliente cliente);
    public int deleteCliente(int id);
    //==========================================FACTURAS

    List<Factura> findAllFacturas();

    public List<Factura> findAllFactura(int documento);
    public List<Factura> findAllFacturaByFecha(String fecha, String fecha2);

    List<Factura> findAllFacturaByCedula(int documento);

    public Factura findAllFacturaByFechaHora(String fecha);


    int saveFactura(Factura factura);

    public int updateFactura(Factura factura);

    public int deleteFactura(int id);
    //========================================USUARIOS=====================================
    public List<Usuario> findAllUsuario(int documento);
    public int updateUsuario(Usuario usuario);
    public Usuario findByEmail(String email);


    int saveUsuario(Usuario usuario);
    public Usuario findByContraseña2(String password);

    int deleteUsuario(int id);

    List<Usuario> findAllUsuarios();

    Double calcularSaldoTotalCliente(int clienteId);

    List<Historial> findHistorial(int documento);

    int saveHistorial(Historial historial);

    int updateHistorial(Historial historial);


    List<Observaciones> findObservacion(int documento);

    int saveObservacion(Observaciones observaciones);

    int deleteobservaciones(int id);

    List<byte[]> findImageHistorial(int id);


}
