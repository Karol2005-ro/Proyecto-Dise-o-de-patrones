package educatec.modelo.entidad;

public class Curso {
    private int id;
    private String codigo;
    private String nombre;
    private int creditos;
    private int horasLectivas;
    private int idCarrera;
    private String codigoPrerrequisito;

    public Curso() {
    }

    public Curso(int id, String codigo, String nombre, int creditos, int horasLectivas,
                 int idCarrera, String codigoPrerrequisito) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasLectivas = horasLectivas;
        this.idCarrera = idCarrera;
        this.codigoPrerrequisito = codigoPrerrequisito;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public int getHorasLectivas() { return horasLectivas; }
    public void setHorasLectivas(int horasLectivas) { this.horasLectivas = horasLectivas; }
    public int getIdCarrera() { return idCarrera; }
    public void setIdCarrera(int idCarrera) { this.idCarrera = idCarrera; }
    public String getCodigoPrerrequisito() { return codigoPrerrequisito; }
    public void setCodigoPrerrequisito(String codigoPrerrequisito) { this.codigoPrerrequisito = codigoPrerrequisito; }

    public boolean tienePrerrequisito() {
        return codigoPrerrequisito != null && !codigoPrerrequisito.isBlank();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
