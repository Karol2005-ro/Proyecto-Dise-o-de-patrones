package educatec.modelo.entidad;

import educatec.modelo.enums.Escala;
import educatec.modelo.enums.ModalidadAlumno;

public class Alumno {
    private int id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private ModalidadAlumno modalidad;
    private Escala escalaPension;
    private int idCarrera;

    public Alumno() {
    }

    public Alumno(int id, String dni, String nombres, String apellidos, String email,
                  ModalidadAlumno modalidad, Escala escalaPension, int idCarrera) {
        this.id = id;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.modalidad = modalidad;
        this.escalaPension = escalaPension;
        this.idCarrera = idCarrera;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public ModalidadAlumno getModalidad() { return modalidad; }
    public void setModalidad(ModalidadAlumno modalidad) { this.modalidad = modalidad; }
    public Escala getEscalaPension() { return escalaPension; }
    public void setEscalaPension(Escala escalaPension) { this.escalaPension = escalaPension; }
    public int getIdCarrera() { return idCarrera; }
    public void setIdCarrera(int idCarrera) { this.idCarrera = idCarrera; }

    public String getNombreCompleto() {
        return apellidos + ", " + nombres;
    }

    @Override
    public String toString() {
        return dni + " - " + getNombreCompleto();
    }
}
