package co.edu.usco.pw.examen.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.usco.pw.examen.model.Rol;
import co.edu.usco.pw.examen.model.Usuario;
import co.edu.usco.pw.examen.repository.RolRepository;
import co.edu.usco.pw.examen.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository,
                                   UsuarioRepository usuarioRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {

            // --- Crear Roles si no existen ---
            Rol rolRector = rolRepository.findByNombre("ROLE_RECTOR")
                    .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_RECTOR")));

            Rol rolDocente = rolRepository.findByNombre("ROLE_DOCENTE")
                    .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_DOCENTE")));

            Rol rolEstudiante = rolRepository.findByNombre("ROLE_ESTUDIANTE")
                    .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_ESTUDIANTE")));

            // --- Crear usuario rector si no existe ---
            if (!usuarioRepository.existsByUsername("rector")) {
                Usuario rector = new Usuario();
                rector.setUsername("rector");
                // Guardar contraseña encriptada
                rector.setPassword(passwordEncoder.encode("rector123"));
                rector.setNombre("Rector");
                rector.setApellido("General");

                Set<Rol> roles = new HashSet<>();
                roles.add(rolRector);
                rector.setRoles(roles);

                usuarioRepository.save(rector);

                System.out.println("✅ Usuario rector creado correctamente (password encriptada).");
            } else {
                System.out.println("ℹ️ Usuario rector ya existe.");
            }
        };
    }
}

