package educatec.negocio.controlador;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Pago;
import educatec.modelo.enums.TipoServicio;
import educatec.negocio.servicio.ServicioAlumno;
import educatec.negocio.servicio.ServicioCurso;
import educatec.negocio.servicio.ServicioMatricula;

import java.util.List;

public class MatriculaController {
    private final ServicioMatricula servicioMatricula;
    private final ServicioAlumno servicioAlumno;
    private final ServicioCurso servicioCurso;

    public MatriculaController(ServicioMatricula servicioMatricula, ServicioAlumno servicioAlumno,
                               ServicioCurso servicioCurso) {
        this.servicioMatricula = servicioMatricula;
        this.servicioAlumno = servicioAlumno;
        this.servicioCurso = servicioCurso;
    }

    public List<Alumno> listarAlumnos() {
        return servicioAlumno.listar();
    }

    public List<Curso> listarCursosDeCarrera(int idCarrera) {
        return servicioCurso.listarPorCarrera(idCarrera);
    }

    public MatriculaResumenDTO matricular(Alumno alumno, List<Curso> cursos, List<TipoServicio> servicios) {
        if (alumno == null) {
            throw new educatec.negocio.excepcion.MatriculaException("Seleccione un alumno.");
        }
        List<Integer> ids = cursos.stream().map(Curso::getId).toList();
        return servicioMatricula.matricular(alumno.getId(), ids, servicios);
    }

    public Pago pagar(int idMatricula, String numeroCuenta) {
        return servicioMatricula.pagar(idMatricula, numeroCuenta == null ? "" : numeroCuenta.trim());
    }

    public void anular(int idMatricula) {
        servicioMatricula.anular(idMatricula);
    }

    public List<MatriculaResumenDTO> listarResumen() {
        return servicioMatricula.listarResumen();
    }
}
