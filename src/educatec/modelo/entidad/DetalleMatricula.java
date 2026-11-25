package educatec.modelo.entidad;

public class DetalleMatricula {
    private int id;
    private int idMatricula;
    private int idCurso;
    private double costoAdicional;
    private String tipoTallerDecorator;

    public DetalleMatricula() {
    }

    public DetalleMatricula(int id, int idMatricula, int idCurso, double costoAdicional, String tipoTallerDecorator) {
        this.id = id;
        this.idMatricula = idMatricula;
        this.idCurso = idCurso;
        this.costoAdicional = costoAdicional;
        this.tipoTallerDecorator = tipoTallerDecorator;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }
    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
    public double getCostoAdicional() { return costoAdicional; }
    public void setCostoAdicional(double costoAdicional) { this.costoAdicional = costoAdicional; }
    public String getTipoTallerDecorator() { return tipoTallerDecorator; }
    public void setTipoTallerDecorator(String tipoTallerDecorator) { this.tipoTallerDecorator = tipoTallerDecorator; }

    public boolean esServicioAdicional() {
        return tipoTallerDecorator != null && !tipoTallerDecorator.isBlank();
    }
}
