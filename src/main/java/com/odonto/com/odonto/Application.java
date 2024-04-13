package com.odonto.com.odonto;

import com.odonto.com.odonto.modelos.Historial;
import com.odonto.com.odonto.repository.OdontoRepositorio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import com.odonto.com.odonto.service.UserDetailsServiceImpl;
import com.odonto.com.odonto.service.UsuarioService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	private final UserDetailsServiceImpl usuarioService;
	private final UsuarioService usuarioService2;
	private final OdontoRepositorio odontoRepositorio;

	public Application(UserDetailsServiceImpl usuarioService, UsuarioService usuarioService2, OdontoRepositorio odontoRepositorio) {
		this.usuarioService = usuarioService;
		this.usuarioService2 = usuarioService2;
		this.odontoRepositorio = odontoRepositorio;
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		Application app = context.getBean(Application.class);
	//	app.calcularSaldo();
	}

/*	public void calcularSaldo() {
		List<Historial> historialExistente = usuarioService2.findAllHistorial(1006166649);

		if (!historialExistente.isEmpty()) {
			Historial primerHistorial = historialExistente.get(0);
			primerHistorial.setIdCliente(1006166649);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

			try {
				// Crear un objeto Date a partir de una cadena con formato 'yyyy-MM-dd HH:mm:ss.SSSSSS'
				Date fecha = dateFormat.parse("2023-10-16 10:30:00.000000");
				primerHistorial.setFecha("2023-10-16 10:30:00.000000");

				primerHistorial.setTipoIdentificacion("DNI");
				primerHistorial.setMotivoDeConsulta("Dolo muelas");
				primerHistorial.setOrigenDeLaEnfermedad("historial.gLaEnfermedad()");
				primerHistorial.setAntecedentesDeImportancia("histordentesDeImportancia()");
				primerHistorial.setObservaciones("historiarvaciones()");
				primerHistorial.setImpresionDiagnostica("historialnDiagnostica()");
				primerHistorial.setValoracionEspecialista("historial.gialista()");

				// Actualizar el historial con la fecha formateada
				odontoRepositorio.updateHistorial(primerHistorial);
			} catch (ParseException e) {
				e.printStackTrace();
				// Manejar la excepción si ocurre un error al analizar la fecha
			}
		}
		System.out.println("El saldo calculado es: " + historialExistente);
	}

*/
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
		return applicationBuilder.sources(Application.class);
	}
}
