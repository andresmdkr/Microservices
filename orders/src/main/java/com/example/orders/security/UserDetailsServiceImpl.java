package com.example.orders.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Carga de usuario por nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var usuario = getById(username);

        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        return User
                .withUsername(username)
                .password(usuario.password())
                .roles(usuario.roles().toArray(new String[0]))
                .build();
    }


    public record Usuario(String username, String password, Set<String> roles) {}

    // Búsqueda de usuario por ID
    public static Usuario getById(String username) {

        var password = "$2a$10$pQw1OckVB1dpd08m775KEe9EE7s/jKwwx5isJcBLWAseUWEk7eQwS"; // Contraseña encriptada del usuario.
        Usuario rootUser = new Usuario(
                "rootUser",
                password,
                Set.of("USER") // Crea un usuario de ejemplo.
        );

        var usuarios = List.of(rootUser); // Lista de usuarios.

        return usuarios // Busca y retorna el usuario que coincide con el nombre de usuario.
                .stream()
                .filter(e -> e.username().equals(username))
                .findFirst()
                .orElse(null);
    }
}
