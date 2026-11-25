package educatec.negocio.controlador;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.TipoDocumento;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioAlumno;
import educatec.negocio.servicio.ServicioDocumentos;

import java.util.List;

public class DocumentosController {
    private final ServicioDocumentos servicioDocumentos;
    private final ServicioAlumno servicioAlumno;

    public DocumentosController(ServicioDocumentos servicioDocumentos, ServicioAlumno servicioAlumno) {
        this.servicioDocumentos = servicioDocumentos;
        this.servicioAlumno = servicioAlumno;
    }

    public List<Alumno> listarAlumnos() {
        return servicioAlumno.listar();
    }

    public String emitir(Alumno alumno, TipoDocumento tipo) {
        if (alumno == null) {
            throw new NegocioException("Seleccione un alumno.");
        }
        return servicioDocumentos.emitir(alumno.getId(), tipo);
    }
}
