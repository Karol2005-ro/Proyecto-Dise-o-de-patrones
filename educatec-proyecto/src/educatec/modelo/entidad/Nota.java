package educatec.modelo.entidad;

import educatec.modelo.enums.TipoEvaluacion;

public class Nota {
    public static final double NOTA_MINIMA = 0;
    public static final double NOTA_MAXIMA = 20;
    public static final double NOTA_APROBATORIA = 11;

    private int id;
    private int idMatricula;
    private int idCurso;
    private double calificacion;
    private TipoEvaluacion evaluacionTipo;

    public Nota() {
    }

    public Nota(int id, int idMatricula, int idCurso, double calificacion, TipoEvaluacion evaluacionTipo) {
        this.id = id;
        this.idMatricula = idMatricula;
        this.idCurso = idCurso;
        this.calificacion = calificacion;
        this.evaluacionTipo = evaluacionTipo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }
    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
    public TipoEvaluacion getEvaluacionTipo() { return evaluacionTipo; }
    public void setEvaluacionTipo(TipoEvaluacion evaluacionTipo) { this.evaluacionTipo = evaluacionTipo; }

    public boolean esAprobada() {
        return calificacion >= NOTA_APROBATORIA;
    }
}
