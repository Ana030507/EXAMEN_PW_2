package co.edu.usco.pw.examen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import co.edu.usco.pw.examen.model.Asignatura;
import co.edu.usco.pw.examen.model.Usuario;
import co.edu.usco.pw.examen.repository.AsignaturaRepository;
import co.edu.usco.pw.examen.repository.UsuarioRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Rector: crear o editar asignaturas
    public Asignatura guardarAsignatura(Asignatura a) {
        return asignaturaRepository.save(a);
    }

    // Rector: eliminar asignatura
    public void eliminarAsignatura(Long id) {
        asignaturaRepository.deleteById(id);
    }

    // Todos: ver todas las asignaturas
    public List<Asignatura> listarAsignaturas() {
        return asignaturaRepository.findAll();
    }

    /**
     * Asigna un docente a una asignatura.
     * Solo puede ser ejecutado por RECTOR.
     *
     * @param asignaturaId id de la asignatura
     * @param docenteId    id del docente a asignar (null para quitar docente)
     */
    public Asignatura asignarDocente(Long asignaturaId, Long docenteId) {
        Asignatura asignatura = asignaturaRepository.findById(asignaturaId)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        if (docenteId != null) {
            Usuario docente = usuarioRepository.findById(docenteId)
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            
            // Verificar que el usuario tenga rol DOCENTE
            boolean esDocente = docente.getRoles().stream()
                    .anyMatch(rol -> rol.getNombre().equals("ROLE_DOCENTE"));
            
            if (!esDocente) {
                throw new RuntimeException("El usuario no tiene rol de docente");
            }
            
            asignatura.setDocenteEncargado(docente);
        } else {
            asignatura.setDocenteEncargado(null);
        }

        return asignaturaRepository.save(asignatura);
    }

    /**
     * Actualiza horarioInicio y horarioFin de la asignatura.
     * El usuario autenticado debe ser el docente encargado.
     *
     * @param id      id de la asignatura
     * @param inicio  cadena "HH:mm" (ej "08:00")
     * @param fin     cadena "HH:mm" (ej "10:00")
     */
    public Asignatura actualizarHorario(Long id, String inicio, String fin) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));

        // obtener username autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (asignatura.getDocenteEncargado() == null) {
            throw new RuntimeException("La asignatura no tiene docente asignado");
        }

        Usuario docente = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // comparar por id (evita nullpointer si username coincidente)
        if (!asignatura.getDocenteEncargado().getId().equals(docente.getId())) {
            throw new RuntimeException("No puedes modificar esta asignatura");
        }

        // parsear y asignar horarios
        LocalTime horaInicio = LocalTime.parse(inicio);
        LocalTime horaFin = LocalTime.parse(fin);

        if (!horaFin.isAfter(horaInicio)) {
            throw new RuntimeException("horarioFin debe ser mayor que horarioInicio");
        }

        asignatura.setHorarioInicio(horaInicio);
        asignatura.setHorarioFin(horaFin);

        return asignaturaRepository.save(asignatura);
    }
}
