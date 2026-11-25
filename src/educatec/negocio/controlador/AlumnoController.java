package educatec.negocio.controlador;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Carrera;
import educatec.modelo.enums.Escala;
import educatec.modelo.enums.ModalidadAlumno;
import educatec.negocio.servicio.ServicioAlumno;
import educatec.negocio.servicio.ServicioCarrera;

import java.util.List;

public class AlumnoController {
    private final ServicioAlumno servicioAlumno;
    private final ServicioCarrera servicioCarrera;

    public AlumnoController(ServicioAlumno servicioAlumno, ServicioCarrera servicioCarrera) {
        this.servicioAlumno = servicioAlumno;
        this.servicioCarrera = servicioCarrera;
    }

    public List<Alumno> listar(String filtro) {
        return servicioAlumno.buscar(filtro);
    }

    public List<Carrera> listarCarreras() {
        return servicioCarrera.listar();
    }

    public Alumno registrar(String dni, String nombres, String apellidos, String email,
                            ModalidadAlumno modalidad, Escala escala, Carrera carrera) {
        return servicioAlumno.registrar(construir(0, dni, nombres, apellidos, email, modalidad, escala, carrera));
    }

    public void actualizar(int id, String dni, String nombres, String apellidos, String email,
                           ModalidadAlumno modalidad, Escala escala, Carrera carrera) {
        servicioAlumno.actualizar(construir(id, dni, nombres, apellidos, email, modalidad, escala, carrera));
    }

    public void eliminar(int id) {
        servicioAlumno.eliminar(id);
    }

    private Alumno construir(int id, String dni, String nombres, String apellidos, String email,
                             ModalidadAlumno modalidad, Escala escala, Carrera carrera) {
        return new Alumno(id, dni.trim(), nombres.trim(), apellidos.trim(), email.trim(), modalidad, escala,
                carrera == null ? 0 : carrera.getId());
    }
}
