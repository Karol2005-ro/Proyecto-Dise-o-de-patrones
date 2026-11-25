package educatec.presentacion.modelotabla;

import educatec.modelo.dto.NotaDetalleDTO;

public class NotaTM extends ModeloTablaGenerico<NotaDetalleDTO> {

    public NotaTM() {
        super("Curso", "Evaluación", "Nota", "Resultado");
    }

    @Override
    protected Object valor(NotaDetalleDTO n, int columna) {
        return switch (columna) {
            case 0 -> n.curso();
            case 1 -> n.evaluacion();
            case 2 -> String.format("%.1f", n.calificacion());
            default -> n.aprobada() ? "Aprobado" : "Desaprobado";
        };
    }
}
