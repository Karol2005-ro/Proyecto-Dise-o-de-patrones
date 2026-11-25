package educatec.modelo.entidad;

import educatec.modelo.enums.EstadoMatricula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Matricula {
    private int id;
    private int idAlumno;
    private String periodoAcademico;
    private LocalDate fechaMatricula;
    private double montoTotal;
    private EstadoMatricula estado;
    private final List<DetalleMatricula> detalles = new ArrayList<>();

    public Matricula() {
    }

    public Matricula(int id, int idAlumno, String periodoAcademico, LocalDate fechaMatricula,
                     double montoTotal, EstadoMatricula estado) {
        this.id = id;
        this.idAlumno = idAlumno;
        this.periodoAcademico = periodoAcademico;
        this.fechaMatricula = fechaMatricula;
        this.montoTotal = montoTotal;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdAlumno() { return idAlumno; }
    public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }
    public String getPeriodoAcademico() { return periodoAcademico; }
    public void setPeriodoAcademico(String periodoAcademico) { this.periodoAcademico = periodoAcademico; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    public EstadoMatricula getEstado() { return estado; }
    public void setEstado(EstadoMatricula estado) { this.estado = estado; }
    public List<DetalleMatricula> getDetalles() { return detalles; }

    public DetalleMatricula agregarCurso(int idCurso) {
        DetalleMatricula detalle = new DetalleMatricula(0, id, idCurso, 0, null);
        detalles.add(detalle);
        return detalle;
    }

    public DetalleMatricula agregarServicio(int idCursoReferencia, double costoAdicional, String tipoDecorator) {
        DetalleMatricula detalle = new DetalleMatricula(0, id, idCursoReferencia, costoAdicional, tipoDecorator);
        detalles.add(detalle);
        return detalle;
    }

    public List<Integer> getIdsCursos() {
        return detalles.stream().map(DetalleMatricula::getIdCurso).distinct().toList();
    }

    public boolean contieneCurso(int idCurso) {
        return detalles.stream().anyMatch(d -> d.getIdCurso() == idCurso);
    }

    public double getCostoAdicionalTotal() {
        return detalles.stream().mapToDouble(DetalleMatricula::getCostoAdicional).sum();
    }

    public boolean estaVigente() {
        return estado != EstadoMatricula.ANULADO;
    }
}
