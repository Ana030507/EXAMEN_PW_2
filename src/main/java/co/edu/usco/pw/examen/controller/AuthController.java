package co.edu.usco.pw.examen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.usco.pw.examen.dto.LoginRequest;
import co.edu.usco.pw.examen.dto.RegisterRequest;
import co.edu.usco.pw.examen.dto.UserResponse;
import co.edu.usco.pw.examen.model.Rol;
import co.edu.usco.pw.examen.model.Usuario;
import co.edu.usco.pw.examen.repository.RolRepository;
import co.edu.usco.pw.examen.service.AuthService;

import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RolRepository rolRepository;

    /**
     * Registro: recibe JSON con username, password, nombre, apellido y opcional "rol" ("estudiante" o "docente").
     * Si no se envía rol, por defecto se registra como ESTUDIANTE.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest req) {
        // Crear entidad Usuario mínima
        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPassword(req.getPassword());
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());

        // determinar rol a asignar
        String requested = req.getRol();
        String rolNombre;
        if (requested == null || requested.trim().isEmpty()) {
            rolNombre = "ROLE_ESTUDIANTE";
        } else {
            rolNombre = "ROLE_" + requested.trim().toUpperCase();
        }

        // Buscar el rol en BD
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

        // Asignar rol al usuario
        u.setRoles(Set.of(rol));

        // Registrar (AuthService encriptará la contraseña)
        Usuario creado = authService.registrarUsuario(u);

        // Preparar respuesta sin password
        UserResponse resp = toUserResponse(creado);
        return ResponseEntity.status(201).body(resp);
    }

    /**
     * Login: delega en AuthService. Si la autenticación falla, Spring Security lanzará excepción.
     * Retorna información básica del usuario (sin contraseña).
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest req) {
        Usuario usuario = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(toUserResponse(usuario));
    }

    // Helper para mapear Usuario -> UserResponse (sin contraseña)
    private UserResponse toUserResponse(Usuario u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setNombre(u.getNombre());
        r.setApellido(u.getApellido());
        if (u.getRoles() != null) {
            Set<String> roles = u.getRoles().stream()
                    .map(Rol::getNombre)
                    .collect(Collectors.toSet());
            r.setRoles(roles);
        }
        return r;
    }
}
