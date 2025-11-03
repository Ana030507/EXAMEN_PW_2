package co.edu.usco.pw.spring_restful_swagger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.usco.pw.spring_restful_swagger.model.Asignatura;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {
    List<Asignatura> findByDocenteEncargadoId(Long docenteId);
}
