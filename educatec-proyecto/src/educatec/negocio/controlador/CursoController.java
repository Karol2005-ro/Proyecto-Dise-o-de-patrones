package educatec.negocio.controlador;

import educatec.modelo.entidad.Carrera;
import educatec.modelo.entidad.Curso;
import educatec.negocio.servicio.ServicioCarrera;
import educatec.negocio.servicio.ServicioCurso;

import java.util.List;

public class CursoController {
    private final ServicioCurso servicioCurso;
    private final ServicioCarrera servicioCarrera;

    public CursoController(ServicioCurso servicioCurso, ServicioCarrera servicioCarrera) {
        this.servicioCurso = servicioCurso;
        this.servicioCarrera = servicioCarrera;
    }

    public List<Curso> listar(String filtro) {
        return servicioCurso.buscar(filtro);
    }

    public List<Carrera> listarCarreras() {
        return servicioCarrera.listar();
    }

    public Curso registrar(String codigo, String nombre, int creditos, int horas, Carrera carrera, String prerrequisito) {
        return servicioCurso.registrar(construir(0, codigo, nombre, creditos, horas, carrera, prerrequisito));
    }

    public void actualizar(int id, String codigo, String nombre, int creditos, int horas, Carrera carrera, String prerrequisito) {
        servicioCurso.actualizar(construir(id, codigo, nombre, creditos, horas, carrera, prerrequisito));
    }

    public void eliminar(int id) {
        servicioCurso.eliminar(id);
    }

    private Curso construir(int id, String codigo, String nombre, int creditos, int horas, Carrera carrera, String prerrequisito) {
        return new Curso(id, codigo.trim().toUpperCase(), nombre.trim(), creditos, horas,
                carrera == null ? 0 : carrera.getId(), prerrequisito == null ? "" : prerrequisito.trim().toUpperCase());
    }
}
