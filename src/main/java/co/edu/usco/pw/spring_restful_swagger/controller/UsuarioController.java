package co.edu.usco.pw.spring_restful_swagger.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.usco.pw.spring_restful_swagger.model.Usuario;
import co.edu.usco.pw.spring_restful_swagger.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public List<Usuario> listarUsuarios() {
		return usuarioService.obtenerUsuarios();
	}

	@Operation(summary = "Actualizar un usuario", description = "Permite actualizar los datos de un usuario existente en la base de datos mediante su ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida") })
	@GetMapping("/{id}")
	public Usuario obtenerUsuario(@PathVariable Long id) {
		return usuarioService.obtenerUsuarioPorId(id);
	}

	@PostMapping
	public Usuario crearUsuario(@RequestBody Usuario usuario) {
		return usuarioService.crearUsuario(usuario);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> actualizarUsuario(
			@Parameter(description = "ID del usuario que se va a actualizar", required = true) @PathVariable Long id,
			@Parameter(description = "Detalles actualizados del usuario") @RequestBody Usuario detallesUsuario) {
		Optional<Usuario> usuarioExistente = usuarioService.findById(id);

		if (usuarioExistente.isPresent()) {
			Usuario usuario = usuarioExistente.get();
			usuario.setNombre(detallesUsuario.getNombre());
			usuario.setEmail(detallesUsuario.getEmail());

			Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuario);
			return ResponseEntity.ok(usuarioActualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public void eliminarUsuario(@PathVariable Long id) {
		usuarioService.eliminarUsuario(id);
	}
}
