package com.odonto.com.odonto.service;

import com.odonto.com.odonto.modelos.Usuario;
import com.odonto.com.odonto.repository.odontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.odonto.com.odonto.repository.odontoRepository;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Service

public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private odontoRepository odontoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = odontoRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new User(usuario.getCorreo(), usuario.getContraseña(), getAuthorities(usuario));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Agregar los roles y permisos del usuario a la lista de authorities
        // Puedes consultar tu base de datos o utilizar lógica personalizada para obtener las autoridades
        if(usuario.getId_vendedor() > 200){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return authorities;

        }else{
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return authorities;

        }
        // Ejemplo: asignar el rol "ROLE_USER" a todos los usuarios


    }
}

