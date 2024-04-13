package com.odonto.com.odonto.controller;

import com.odonto.com.odonto.modelos.RecaptchaResponse;
import com.odonto.com.odonto.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistroController {
    @GetMapping("/")
    public String iniciarSesion2(){

        return "index";
    }

    @GetMapping("/index")
    public String iniciarSesion(){

        return "index";
    }
    @Autowired
    private UserDetailsServiceImpl usuarioService;
    @Value("${recaptcha.secret}")
    private String recaptchaSecretKey;

    @PostMapping("/index")
    public String procesarInicioSesion(HttpServletRequest request, @RequestParam("g-recaptcha-response") String captchaResponse) {
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + recaptchaSecretKey + "&response=" + captchaResponse;
        RecaptchaResponse recaptchaResponse = new RestTemplate().postForObject(url, null, RecaptchaResponse.class);
        if (recaptchaResponse.isSuccess()) {
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
                return "redirect:/index?error";
            }
        } catch (UsernameNotFoundException e) {
            // El usuario no existe
            return "redirect:/index?error";
        }
        } else {
            // CAPTCHA inválido, redirigir de nuevo a la página de inicio de sesión
            return "redirect:/index?error=captcha";
        }
    }
}
