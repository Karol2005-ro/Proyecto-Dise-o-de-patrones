package educatec.presentacion.modelotabla;

import educatec.modelo.entidad.Curso;

import java.util.HashMap;
import java.util.Map;

public class CursoTM extends ModeloTablaGenerico<Curso> {
    private Map<Integer, String> carreras = new HashMap<>();

    public CursoTM() {
        super("Código", "Curso", "Créditos", "Horas", "Prerrequisito", "Carrera");
    }

    public void setCarreras(Map<Integer, String> carreras) {
        this.carreras = carreras;
        fireTableDataChanged();
    }

    @Override
    protected Object valor(Curso c, int columna) {
        return switch (columna) {
            case 0 -> c.getCodigo();
            case 1 -> c.getNombre();
            case 2 -> c.getCreditos();
            case 3 -> c.getHorasLectivas();
            case 4 -> c.tienePrerrequisito() ? c.getCodigoPrerrequisito() : "-";
            default -> carreras.getOrDefault(c.getIdCarrera(), "-");
        };
    }
}
