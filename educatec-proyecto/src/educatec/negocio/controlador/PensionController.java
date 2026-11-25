package educatec.negocio.controlador;

import educatec.modelo.dto.ReportePensionDTO;
import educatec.modelo.enums.Escala;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioPension;

import java.util.List;

public class PensionController {
    private final ServicioPension servicioPension;

    public PensionController(ServicioPension servicioPension) {
        this.servicioPension = servicioPension;
    }

    public List<ReportePensionDTO> generarReporte(String montoBase) {
        double monto;
        try {
            monto = Double.parseDouble(montoBase.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new NegocioException("El monto base debe ser un número válido.");
        }
        return servicioPension.generarReporte(monto);
    }

    public void cambiarEscala(int idAlumno, Escala escala) {
        servicioPension.cambiarEscala(idAlumno, escala);
    }
}
