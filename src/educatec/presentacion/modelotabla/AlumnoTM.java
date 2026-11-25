package educatec.presentacion.modelotabla;

import educatec.modelo.entidad.Alumno;

import java.util.HashMap;
import java.util.Map;

public class AlumnoTM extends ModeloTablaGenerico<Alumno> {
    private Map<Integer, String> carreras = new HashMap<>();

    public AlumnoTM() {
        super("DNI", "Apellidos y nombres", "Correo", "Modalidad", "Escala de pensión", "Carrera");
    }

    public void setCarreras(Map<Integer, String> carreras) {
        this.carreras = carreras;
        fireTableDataChanged();
    }

    @Override
    protected Object valor(Alumno a, int columna) {
        return switch (columna) {
            case 0 -> a.getDni();
            case 1 -> a.getNombreCompleto();
            case 2 -> a.getEmail();
            case 3 -> a.getModalidad();
            case 4 -> a.getEscalaPension();
            default -> carreras.getOrDefault(a.getIdCarrera(), "-");
        };
    }
}
