package co.edu.usco.pw.examen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import co.edu.usco.pw.examen.dto.HorarioDto;
import co.edu.usco.pw.examen.model.Asignatura;
import co.edu.usco.pw.examen.service.AsignaturaService;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    // --- Rector: crear o editar ---
    @PreAuthorize("hasRole('RECTOR')")
    @PostMapping
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody Asignatura a) {
        return ResponseEntity.ok(asignaturaService.guardarAsignatura(a));
    }

    @PreAuthorize("hasRole('RECTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Asignatura> editarAsignatura(@PathVariable Long id, @RequestBody Asignatura a) {
        a.setId(id);
        return ResponseEntity.ok(asignaturaService.guardarAsignatura(a));
    }

    // --- Rector: eliminar ---
    @PreAuthorize("hasRole('RECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignatura(@PathVariable Long id) {
        asignaturaService.eliminarAsignatura(id);
        return ResponseEntity.noContent().build();
    }

    // --- Todos: ver ---
    @GetMapping
    @PreAuthorize("hasAnyRole('RECTOR', 'DOCENTE', 'ESTUDIANTE')")
    public ResponseEntity<List<Asignatura>> listarAsignaturas() {
        return ResponseEntity.ok(asignaturaService.listarAsignaturas());
    }

    // --- Rector: asignar docente a asignatura ---
    @PreAuthorize("hasRole('RECTOR')")
    @PatchMapping("/{id}/docente")
    public ResponseEntity<Asignatura> asignarDocente(
            @PathVariable Long id,
            @RequestParam(required = false) Long docenteId) {
        Asignatura actualizado = asignaturaService.asignarDocente(id, docenteId);
        return ResponseEntity.ok(actualizado);
    }

    // --- Docente: actualizar horarios solo de sus asignaturas ---
    @PreAuthorize("hasRole('DOCENTE')")
    @PatchMapping("/{id}/horarios")
    public ResponseEntity<Asignatura> actualizarHorarios(
            @PathVariable Long id,
            @RequestBody HorarioDto horarioDto) {

        Asignatura actualizado = asignaturaService.actualizarHorario(id, horarioDto.getInicio(), horarioDto.getFin());
        return ResponseEntity.ok(actualizado);
    }

}
