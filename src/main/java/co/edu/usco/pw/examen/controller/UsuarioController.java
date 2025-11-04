package co.edu.usco.pw.examen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.usco.pw.examen.dto.UserResponse;
import co.edu.usco.pw.examen.model.Rol;
import co.edu.usco.pw.examen.model.Usuario;
import co.edu.usco.pw.examen.service.UsuarioService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para que el RECTOR obtenga la lista de docentes disponibles
     * para asignarlos a asignaturas
     */
    @PreAuthorize("hasRole('RECTOR')")
    @GetMapping("/docentes")
    public ResponseEntity<List<UserResponse>> listarDocentes() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        
        // Filtrar solo los usuarios que tienen el rol DOCENTE
        List<UserResponse> docentes = usuarios.stream()
            .filter(u -> u.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_DOCENTE")))
            .map(this::toUserResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(docentes);
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
