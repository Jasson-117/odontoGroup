package com.odonto.com.odonto.service;

import com.odonto.com.odonto.modelos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.odonto.com.odonto.repository.odontoRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements odontoService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private odontoRepository odontoRepository;
    @Override
    public List<Cliente> findAllClientes() {
        List<Cliente> list;
        try {
            list = odontoRepository.findAllClientes();

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }

    @Override
    public List<Cliente> findAllsCliente() {

        List<Cliente> list;
        try {
            list = odontoRepository.findAllClientes();

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public Double calcularSaldo(int documento) {

        Double list;
        try {
            list = odontoRepository.calcularSaldoTotalCliente(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }

    @Override
    public List<Cliente> findAllCliente(int documento) {
        List<Cliente> list;
        try {
            list = odontoRepository.findAllCliente(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public List<Cliente> findAllClienteByNombre(String nombre) {
        List<Cliente> list;
        try {
            list = odontoRepository.findAllClienteByNombre(nombre);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }

    @Override
    public int saveCliente(Cliente cliente) {
        int row;
        try {
            row = odontoRepository.saveCliente(cliente);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }

    @Override
    public int updateCliente(Cliente cliente) {
        int row;
        try {
            row = odontoRepository.updateCliente(cliente);

        }catch (Exception ex){
            throw ex;
        }
        return row;       }

    @Override
    public int deleteCliente(int id) {
        int row;
        try {
            row = odontoRepository.deleteCliente(id);

        }catch (Exception ex){
            throw ex;
        }
        return row;       }
    //==================================================Factura===========================================

    @Override
    public List<Factura> findAllFacturas() {
        List<Factura> list;
        try {
            list = odontoRepository.findAllFacturas();

        }catch (Exception ex){
            throw ex;
        }
        return list;    }

    @Override
    public List<Factura> findAllsFactura() {
        List<Factura> list;
        try {
            list = odontoRepository.findAllFacturas();

        }catch (Exception ex){
            throw ex;
        }
        return list;    }

    @Override
    public List<Factura> findAllFactura(int id_factura) {
        List<Factura> list;
        try {
            list = odontoRepository.findAllFactura(id_factura);

        }catch (Exception ex){
            throw ex;
        }
        return list;    }

    @Override
    public int saveFactura(Factura factura) {
        int row;
        try {
            row = odontoRepository.saveFactura(factura);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }

    @Override
    public int updateFactura(Factura factura) {
        int row;
        try {
            row = odontoRepository.updateFactura(factura);

        }catch (Exception ex){
            throw ex;
        }
        return row;       }

    @Override
    public int deleteFactura(int id) {
        int row;
        try {
            row = odontoRepository.deleteFactura(id);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }

    @Override
    public Factura findAllFacturaByFecha(String fecha) {
        return null;
    }

    @Override
    public Factura findAllFacturaByFechaHora(String fecha) {
        return null;
   }

    @Override
    public Factura findAllFacturaByFecha2(String fecha) {
        Factura list;
        try {
            list = odontoRepository.findAllFacturaByFechaHora(fecha);

        }catch (Exception ex){
            throw ex;
        }
        return list;      }

    //=========================================================================USUARIOS=============================

    @Override
    public List<Usuario> findAllUsuario(int documento) {
        List<Usuario> list;
        try {
            list = odontoRepository.findAllUsuario(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;       }

    @Override
    public int updateUsuario(Usuario usuario) {
        int row;
        try {
            row = odontoRepository.updateUsuario(usuario);

        }catch (Exception ex){
            throw ex;
        }
        return row;       }
    @Override
    public int saveUsuario(Usuario usuario) {
        int row;
        try {
            row = odontoRepository.saveUsuario(usuario);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }

    @Override
    public Usuario findAllUsuarioByPassword(String documento) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = odontoRepository.findByEmail(username);
        System.out.print(usuario);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o consatresa invalidos");
        }
        return new User(usuario.getCorreo(),usuario.getContraseña(),mapearAutoridadesARoles(usuario.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapearAutoridadesARoles(Collection<Rol> roles){
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
    }

    @Override
    public Usuario findAllUsuarioByEmail(String documento) {
        Usuario list;
        try {
            list = odontoRepository.findByContraseña2(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;       }
    @Override
    public List<Factura> findAllFacturaByFecha(String fecha1, String fecha2 ) {
        List<Factura> list;
        try {
            list = odontoRepository.findAllFacturaByFecha(fecha1,fecha2);

        }catch (Exception ex){
            throw ex;
        }
        return list;       }
    @Override
    public int deleteUsuario(int id) {
        int row;
        try {
            row = odontoRepository.deleteUsuario(id);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }
    @Override
    public List<Usuario> findAllUsuario2s() {
        List<Usuario> list;
        try {
            list = odontoRepository.findAllUsuarios();

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public List<Historial> findAllHistorial(int documento) {
        List<Historial> list;
        try {
            list = odontoRepository.findHistorial(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }



    @Override
    public int updateHistorial(Historial historial) {
        int row;
        try {
            row = odontoRepository.updateHistorial(historial);

        }catch (Exception ex){
            throw ex;
        }
        return row;       }
    @Override
    public int saveHistorial(Historial historial) {
        int row;
        try {
            row = odontoRepository.saveHistorial(historial);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }
    @Override
    public List<Observaciones> findAllObservaciones(int documento) {
        List<Observaciones> list;
        try {
            list = odontoRepository.findObservacion(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public int saveObservacion(Observaciones observaciones) {
        int row;
        try {
            row = odontoRepository.saveObservacion(observaciones);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }
    @Override
    public int deleteObservacion(int id) {
        int row;
        try {
            row = odontoRepository.deleteobservaciones(id);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }
    @Override
    public Usuario findUserByEmail(String email) {
        Usuario list;
        try {
            list = odontoRepository.findByEmail(email);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public List<Factura> findAllFacturasByDocumento(int documento) {
        List<Factura> list;
        try {
            list = odontoRepository.findAllFacturaByCedula(documento);

        }catch (Exception ex){
            throw ex;
        }
        return list;
    }
    @Override
    public List<byte[]> obtenerImagenPorId(int id) {
        List<byte[]> row;
        try {
            row = odontoRepository.findImageHistorial(id);

        }catch (Exception ex){
            throw ex;
        }
        return row;
    }
}
