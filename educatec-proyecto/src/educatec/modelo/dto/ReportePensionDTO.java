package educatec.modelo.dto;

import educatec.modelo.enums.Escala;

public record ReportePensionDTO(int idAlumno, String alumno, Escala escala,
                                double montoBase, double descuento, double montoFinal) {
}
