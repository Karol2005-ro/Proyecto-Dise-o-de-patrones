package educatec.presentacion.modelotabla;

import educatec.modelo.dto.MatriculaResumenDTO;

public class MatriculaTM extends ModeloTablaGenerico<MatriculaResumenDTO> {

    public MatriculaTM() {
        super("N°", "Alumno", "DNI", "Periodo", "Fecha", "Cursos", "Créditos", "Monto (S/)", "Estado", "Promedio");
    }

    @Override
    protected Object valor(MatriculaResumenDTO m, int columna) {
        return switch (columna) {
            case 0 -> m.idMatricula();
            case 1 -> m.alumno();
            case 2 -> m.dni();
            case 3 -> m.periodo();
            case 4 -> m.fecha();
            case 5 -> m.totalCursos();
            case 6 -> m.totalCreditos();
            case 7 -> String.format("%.2f", m.montoTotal());
            case 8 -> m.estado();
            default -> m.tienePromedio() ? String.format("%.2f", m.promedio()) : "-";
        };
    }
}
