package co.edu.usco.pw.spring_restful_swagger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsignaturaDto {
    private String nombre;
    private String descripcion;
    private Integer salon;
    // Horarios como "HH:mm"
    private String horarioInicio;
    private String horarioFin;
    // docenteId opcional: si está presente, se usará (si no está, no se toca).
    private Long docenteId;

    // Helper: para distinguir entre ausencia y null explícito en JSON (si se quiere quitar docente)
    private Boolean docenteIdPresent;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getSalon() { return salon; }
    public void setSalon(Integer salon) { this.salon = salon; }
    public String getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(String horarioInicio) { this.horarioInicio = horarioInicio; }
    public String getHorarioFin() { return horarioFin; }
    public void setHorarioFin(String horarioFin) { this.horarioFin = horarioFin; }
    public Long getDocenteId() { return docenteId; }
    public void setDocenteId(Long docenteId) { this.docenteId = docenteId; this.docenteIdPresent = true; }
    public boolean isDocenteIdPresent() { return Boolean.TRUE.equals(this.docenteIdPresent); }
}
