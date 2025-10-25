package co.edu.usco.pw.spring_restful_swagger.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.usco.pw.spring_restful_swagger.model.Usuario;
import co.edu.usco.pw.spring_restful_swagger.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> obtenerUsuarios() {
		return usuarioRepository.findAll();
	}

	public Usuario obtenerUsuarioPorId(Long id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	public Usuario crearUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public Usuario updateUsuario(Long id, Usuario productoDetails) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow();
		usuario.setNombre(productoDetails.getNombre());
		usuario.setEmail(productoDetails.getEmail());
		return usuarioRepository.save(usuario);
	}

	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}

	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}
}
