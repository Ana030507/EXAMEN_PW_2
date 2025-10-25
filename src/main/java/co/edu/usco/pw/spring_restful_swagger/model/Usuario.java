package co.edu.usco.pw.spring_restful_swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
@Schema(description = "Entidad que repersenta un usuario en el sistema.")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "ID único del usuario (llave primaria)", example = "1", required = true)
	private Long id;

	@Schema(description = "Nombre del usuario", example = "Juan Pérez", required = true)
	@Column(nullable = false)
	private String nombre;

	@Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com", required = true)
	@Column(nullable = false)
	private String email;
}
