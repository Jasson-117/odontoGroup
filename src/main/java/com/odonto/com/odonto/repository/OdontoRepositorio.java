package com.odonto.com.odonto.repository;

import com.odonto.com.odonto.modelos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OdontoRepositorio implements odontoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Cliente> findAllClientes() {
        String SQL = "SELECT * FROM cliente";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Cliente.class));
    }
    public List<Cliente> findAllCliente(int documento) {
        String SQL = "SELECT * FROM cliente WHERE documento = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Cliente.class), documento);
    }
    @Override
    public List<Cliente> findAllClienteByNombre(String nombre) {
        String SQL = "SELECT * FROM cliente WHERE nombre = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Cliente.class), nombre);
    }


    @Override
    public int saveCliente(Cliente cliente) {
        String SQL = "INSERT INTO cliente VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{cliente.getDocumento(),cliente.getNombre(),cliente.getApellido(),
                cliente.getCorreo(),cliente.getDireccion(),cliente.getTelefono()});
    }

    @Override
    public int updateCliente(Cliente cliente) {
        String SQL = "UPDATE cliente SET documento=?, nombre=?, apellido=?, correo=?, direccion=?, telefono=? WHERE documento=?";
        return jdbcTemplate.update(SQL, new Object[]{cliente.getDocumento(),cliente.getNombre(),cliente.getApellido(),
                cliente.getCorreo(),cliente.getDireccion(),cliente.getTelefono(),cliente.getDocumento()});
    }

    @Override
    public int deleteCliente(int id) {
        String SQL = "DELETE FROM cliente WHERE documento=?";
        return jdbcTemplate.update(SQL, new Object[]{id});
    }
    //===========================================FACTURA====================================================================

    @Override
    public List<Factura> findAllFacturas() {
        String SQL = "SELECT * FROM factura";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Factura.class));
    }

    @Override
    public List<Factura> findAllFactura(int factura_id) {
        String SQL = "SELECT * FROM factura WHERE factura_id = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Factura.class), factura_id);
    }

    @Override
    public List<Factura> findAllFacturaByFecha(String fecha1, String fecha2) {
        String SQL = "SELECT * FROM factura WHERE fecha BETWEEN ? AND ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Factura.class),fecha1,fecha2);
    }
    @Override
    public List<Factura> findAllFacturaByCedula(int documento) {
        String SQL = "SELECT * FROM factura WHERE id_cliente = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Factura.class),documento);
    }

    @Override
    public Factura findAllFacturaByFechaHora(String fecha) {
        String SQL = "SELECT * FROM factura WHERE fecha = ?";
        List<Factura> facturas = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Factura.class), fecha);

        if (facturas.isEmpty()) {
            return null; // No se encontró ningún usuario con el correo electrónico especificado
        } else {
            return facturas.get(0); // Devuelve el primer usuario encontrado
        }
    }


    @Override
    public int saveFactura(Factura factura) {
        String SQL = "INSERT INTO factura (prospecto, id_cliente, nombreCliente, nombreVendedor, id_vendedor, fecha, total, saldo) VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{factura.getProspecto(),factura.getId_cliente(),factura.getNombreCliente(),
                factura.getNombreVendedor(),factura.getId_vendedor(),factura.getFecha(),factura.getTotal(),factura.getSaldo()});
    }

    @Override
    public int updateFactura(Factura factura) {
        String SQL = "UPDATE factura SET prospecto=?, id_cliente=?, nombreCliente=?, nombreVendedor=?, id_vendedor=?, fecha=?, total=?,saldo=? WHERE factura_id=?";
        return jdbcTemplate.update(SQL, new Object[]{factura.getProspecto(),factura.getId_cliente(),factura.getNombreCliente(),
                factura.getNombreVendedor(),factura.getId_vendedor(),factura.getFecha(),factura.getTotal(),factura.getSaldo(),factura.getFactura_id()});
    }
    @Override
    public int deleteFactura(int id) {

        String SQL = "DELETE FROM factura WHERE factura_id=?";
        return jdbcTemplate.update(SQL, new Object[]{id});
    }
    //================================================USUARIOS============================================================

    @Override
    public List<Usuario> findAllUsuario(int documento) {
        String SQL = "SELECT * FROM usuario WHERE documento = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class), documento);
    }

    @Override
    public int updateUsuario(Usuario usuario) {
        String SQL = "UPDATE usuario SET documento=?, id_vendedor=?, nombre=?, apellido=?, contraseña=?, correo=? WHERE documento=?";
        return jdbcTemplate.update(SQL, new Object[]{usuario.getDocumento(),usuario.getId_vendedor(),usuario.getNombre(),
                usuario.getApellido(),
                usuario.getContraseña(),usuario.getCorreo(),usuario.getDocumento()});
    }

    @Override
    public Usuario findByEmail(String email) {
        String SQL = "SELECT * FROM usuario WHERE correo = ?";
        List<Usuario> usuarios = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class), email);

        if (usuarios.isEmpty()) {
            return null; // No se encontró ningún usuario con el correo electrónico especificado
        } else {
            return usuarios.get(0); // Devuelve el primer usuario encontrado
        }
    }

    @Override
    public int saveUsuario(Usuario usuario) {
        String SQL = "INSERT INTO usuario VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{usuario.getDocumento(),usuario.getId_vendedor(),usuario.getNombre(),
                usuario.getApellido(),usuario.getContraseña(),usuario.getCorreo()});

    }


    @Override
    public Usuario findByContraseña2(String password) {
        String SQL = "SELECT documento FROM usuario WHERE correo = ?";
        List<Usuario> usuarios = jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class), password);

        if (usuarios.isEmpty()) {
            return null; // No se encontró ningún usuario con el correo electrónico especificado
        } else {
            return usuarios.get(0); // Devuelve el primer usuario encontrado
        }    }
    @Override
    public int deleteUsuario(int id) {
        String SQL = "DELETE FROM usuario WHERE documento=?";
        return jdbcTemplate.update(SQL, new Object[]{id});
    }
    @Override
    public List<Usuario> findAllUsuarios() {
        String SQL = "SELECT * FROM usuario";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Usuario.class));
    }
    @Override
    public Double calcularSaldoTotalCliente(int clienteId) {
        String SQL = "SELECT SUM(saldo) FROM factura WHERE id_cliente = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[] { clienteId }, Double.class);
    }

    //================================================HISTORIALES============================================================

    @Override
    public List<Historial> findHistorial(int documento) {
        String SQL = "SELECT * FROM historial_medico WHERE id_cliente = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Historial.class), documento);
    }
    @Override
    public int saveHistorial(Historial historial) {
        String SQL = "INSERT INTO historial_medico (id_cliente, fecha, tipo_identificacion, motivo_de_consulta, origen_de_la_enfermedad, historia_de_enfermedad_actual, antecedentes_de_importancia, observaciones, impresion_diagnostica, valoracion_especialista, image) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{historial.getIdCliente(),historial.getFecha(),historial.getTipoIdentificacion(),
                historial.getMotivoDeConsulta(),historial.getOrigenDeLaEnfermedad(),historial.getHistoriaDeEnfermedadActual(),historial.getAntecedentesDeImportancia(),historial.getObservaciones(),historial.getImpresionDiagnostica(),historial.getValoracionEspecialista(),historial.getImagen()});

    }
    @Override
    public int updateHistorial(Historial historial) {
        String SQL = "UPDATE historial_medico " +
                "SET  tipo_identificacion = ?, motivo_de_consulta = ?, " +
                "origen_de_la_enfermedad = ?, historia_de_enfermedad_actual = ?, " +
                "antecedentes_de_importancia = ?, observaciones = ?, " +
                "impresion_diagnostica = ?, valoracion_especialista = ?, " +
                "image = ?"+
                "WHERE id_cliente = ?";
        return jdbcTemplate.update(SQL, new Object[]{historial.getTipoIdentificacion(), historial.getMotivoDeConsulta(), historial.getOrigenDeLaEnfermedad(), historial.getHistoriaDeEnfermedadActual(), historial.getAntecedentesDeImportancia(), historial.getObservaciones(), historial.getImpresionDiagnostica(), historial.getValoracionEspecialista(),1006166649});

    }
  // ===================================================================================================================
    @Override
    public List<Observaciones> findObservacion(int documento) {
        String SQL = "SELECT * FROM observaciones WHERE id_cliente = ?";
        return jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(Observaciones.class), documento);
    }
    @Override
    public int saveObservacion(Observaciones observaciones) {
        String SQL = "INSERT INTO observaciones (observacion, fecha, id_cliente) VALUES(?,?,?)";
        return jdbcTemplate.update(SQL, new Object[]{observaciones.getObservacion(),observaciones.getFecha(),observaciones.getIdCliente()});

    }
    @Override
    public int deleteobservaciones(int id) {
        String SQL = "DELETE FROM observaciones WHERE id_cliente=?";
        return jdbcTemplate.update(SQL, new Object[]{id});
    }



    @Override
    public List<byte[]> findImageHistorial(int id) {
        String SQL = "SELECT image FROM historial_medico WHERE id_cliente=?";
        return jdbcTemplate.query(SQL, new Object[]{id}, (rs, rowNum) -> rs.getBytes("image"));
    }

}
