package educatec.negocio.servicio;

import educatec.modelo.dto.ReportePensionDTO;
import educatec.modelo.enums.Escala;

import java.util.List;

public interface ServicioPension {
    List<ReportePensionDTO> generarReporte(double montoBase);

    void cambiarEscala(int idAlumno, Escala escala);
}
