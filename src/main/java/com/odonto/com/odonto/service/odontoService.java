package com.odonto.com.odonto.service;

import com.odonto.com.odonto.modelos.Cliente;
import com.odonto.com.odonto.modelos.Factura;
import com.odonto.com.odonto.modelos.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.List;

public interface odontoService extends UserDetailsService {
    public List<Cliente> findAllClientes();
    public List<Cliente> findAllsCliente();

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
}
