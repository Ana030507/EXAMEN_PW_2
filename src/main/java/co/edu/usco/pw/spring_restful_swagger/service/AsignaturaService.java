package co.edu.usco.pw.spring_restful_swagger.service;

import co.edu.usco.pw.spring_restful_swagger.model.Asignatura;
import co.edu.usco.pw.spring_restful_swagger.model.Usuario;
import co.edu.usco.pw.spring_restful_swagger.repository.AsignaturaRepository;
import co.edu.usco.pw.spring_restful_swagger.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
