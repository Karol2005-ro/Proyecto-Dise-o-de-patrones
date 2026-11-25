package educatec.negocio.controlador;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.dto.NotaDetalleDTO;
import educatec.modelo.entidad.Curso;
import educatec.modelo.enums.TipoEvaluacion;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioNotas;

import java.util.List;

public class NotasController {
    private final ServicioNotas servicioNotas;

    public NotasController(ServicioNotas servicioNotas) {
        this.servicioNotas = servicioNotas;
    }

    public List<MatriculaResumenDTO> listarMatriculasPagadas() {
        return servicioNotas.listarMatriculasPagadas();
    }

    public List<Curso> listarCursos(int idMatricula) {
        return servicioNotas.listarCursosDeMatricula(idMatricula);
    }

    public void registrarNota(int idMatricula, Curso curso, String calificacion, TipoEvaluacion tipo) {
        if (curso == null) {
            throw new NegocioException("Seleccione un curso.");
        }
        double valor;
        try {
            valor = Double.parseDouble(calificacion.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new NegocioException("La nota debe ser un número válido.");
        }
        servicioNotas.registrarNota(idMatricula, curso.getId(), valor, tipo);
    }

    public List<NotaDetalleDTO> listarNotas(int idMatricula) {
        return servicioNotas.listarNotas(idMatricula);
    }

    public double promedio(int idMatricula) {
        return servicioNotas.promedio(idMatricula);
    }

    public List<String> notificaciones() {
        return servicioNotas.notificaciones();
    }
}
