package educatec.modelo.dto;

import educatec.modelo.enums.EstadoMatricula;

import java.time.LocalDate;

public record MatriculaResumenDTO(int idMatricula, String alumno, String dni, String periodo,
                                  LocalDate fecha, int totalCursos, int totalCreditos,
                                  double montoTotal, EstadoMatricula estado, double promedio) {
    public boolean tienePromedio() {
        return promedio >= 0;
    }
}
