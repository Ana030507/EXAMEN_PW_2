package co.edu.usco.pw.examen.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Nombre de usuario", example = "amaria")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario", example = "123456")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Ana María")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Cabrera")
    private String apellido;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
    
    /**
     * Relación inversa opcional: un docente puede tener varias asignaturas.
     * FetchType.LAZY para evitar cargar siempre las asignaturas al traer el usuario.
     * @JsonIgnore evita referencias circulares al serializar a JSON.
     */
    @OneToMany(mappedBy = "docenteEncargado", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Asignatura> asignaturasACargo = new HashSet<>();
}