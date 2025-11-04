package co.edu.usco.pw.examen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.usco.pw.examen.model.Usuario;
import co.edu.usco.pw.examen.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registro (simple): encripta pass antes de guardar
    public Usuario registrarUsuario(Usuario u) {
        if (usuarioRepository.existsByUsername(u.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return usuarioRepository.save(u);
    }

    // Login: delegamos a AuthenticationManager
    public Usuario login(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        // Si llega aquí, la autenticación fue exitosa
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

