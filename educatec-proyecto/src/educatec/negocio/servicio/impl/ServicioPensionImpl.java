package educatec.negocio.servicio.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.modelo.dto.ReportePensionDTO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.Escala;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.patrones.strategy.ContextoPension;
import educatec.negocio.servicio.ServicioPension;

import java.util.List;

public class ServicioPensionImpl implements ServicioPension {
    private final IAlumnoDAO alumnoDAO;
    private final ContextoPension contexto;

    public ServicioPensionImpl(IAlumnoDAO alumnoDAO, ContextoPension contexto) {
        this.alumnoDAO = alumnoDAO;
        this.contexto = contexto;
    }

    @Override
    public List<ReportePensionDTO> generarReporte(double montoBase) {
        if (montoBase <= 0) {
            throw new NegocioException("El monto base de la pensión debe ser mayor a cero.");
        }
        return alumnoDAO.listarTodos().stream().map(a -> {
            double finalPension = contexto.calcular(a, montoBase);
            double descuento = Math.round((montoBase - finalPension) * 100.0) / 100.0;
            return new ReportePensionDTO(a.getId(), a.getNombreCompleto(), a.getEscalaPension(),
                    montoBase, descuento, finalPension);
        }).toList();
    }

    @Override
    public void cambiarEscala(int idAlumno, Escala escala) {
        if (escala == null) {
            throw new NegocioException("Seleccione una escala de pensión.");
        }
        Alumno alumno = alumnoDAO.buscarPorId(idAlumno)
                .orElseThrow(() -> new NegocioException("El alumno seleccionado no existe."));
        alumno.setEscalaPension(escala);
        alumnoDAO.actualizar(alumno);
    }
}
