package com.odonto.com.odonto.controller;

import com.odonto.com.odonto.service.UserDetailsImpl;
import com.odonto.com.odonto.service.UserDetailsServiceImpl;
import com.odonto.com.odonto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistroController {

    @GetMapping("/login")
    public String iniciarSesion(){

        return "login";
    }
    @Autowired
    private UserDetailsServiceImpl usuarioService;
    @GetMapping("/")
    public String mostrarInicio(){
        return "index";

    }
    @PostMapping("/login")
    public String procesarInicioSesion(HttpServletRequest request) {

        // Obtener el nombre de usuario y contraseña proporcionados por el usuario
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Autenticar al usuario utilizando el UserDetailsService personalizado

            UserDetails userDetails = usuarioService.loadUserByUsername(username);

            // Verificar la contraseña
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                // La contraseña es válida

                // Crear un objeto Authentication
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

                // Establecer el objeto Authentication en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Redireccionar al usuario a la página deseada después del inicio de sesión exitoso
                return "redirect:/clientes";
            } else {
                // La contraseña es incorrecta
                return "redirect:/login?error";
            }
        } catch (UsernameNotFoundException e) {
            // El usuario no existe
            return "redirect:/login?error";
        }
    }
}
