package co.edu.usco.pw.examen.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "asignaturas")
@Schema(description = "Entidad que representa una asignatura del sistema")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la asignatura", example = "1")
    private Long id;

    @Column(length = 30, nullable = false)
    @Schema(description = "Nombre de la asignatura", example = "Matemáticas")
    private String nombre;

    @Column(length = 100)
    @Schema(description = "Descripción de la asignatura", example = "Curso básico de álgebra")
    private String descripcion;

    @Schema(description = "Número de salón", example = "101")
    private Integer salon;

    @Schema(description = "Hora de inicio", example = "08:00")
    private LocalTime horarioInicio;

    @Schema(description = "Hora de fin", example = "10:00")
    private LocalTime horarioFin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id", nullable = true)
    @Schema(description = "Docente encargado de la asignatura")
    private Usuario docenteEncargado;
}
