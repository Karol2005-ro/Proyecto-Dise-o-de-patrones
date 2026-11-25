package educatec.negocio.servicio;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.entidad.Pago;
import educatec.modelo.enums.TipoServicio;

import java.util.List;

public interface ServicioMatricula {
    MatriculaResumenDTO matricular(int idAlumno, List<Integer> idsCursos, List<TipoServicio> servicios);

    Pago pagar(int idMatricula, String numeroCuenta);

    void anular(int idMatricula);

    List<MatriculaResumenDTO> listarResumen();
}
