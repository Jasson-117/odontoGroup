package com.odonto.com.odonto;


import com.odonto.com.odonto.controller.ClienteController;
import com.odonto.com.odonto.modelos.Cliente;
import com.odonto.com.odonto.modelos.Usuario;
import com.odonto.com.odonto.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private final ClienteController clienteController;
	@Autowired
	private UserDetailsServiceImpl usuarioService;

	public Application(ClienteController clienteController) {
		this.clienteController = clienteController;
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());



	}
}
