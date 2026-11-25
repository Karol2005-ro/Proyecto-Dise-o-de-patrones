package educatec.negocio.servicio;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.dto.NotaDetalleDTO;
import educatec.modelo.entidad.Curso;
import educatec.modelo.enums.TipoEvaluacion;

import java.util.List;

public interface ServicioNotas {
    List<MatriculaResumenDTO> listarMatriculasPagadas();

    List<Curso> listarCursosDeMatricula(int idMatricula);

    void registrarNota(int idMatricula, int idCurso, double calificacion, TipoEvaluacion tipo);

    List<NotaDetalleDTO> listarNotas(int idMatricula);

    double promedio(int idMatricula);

    List<String> notificaciones();
}
