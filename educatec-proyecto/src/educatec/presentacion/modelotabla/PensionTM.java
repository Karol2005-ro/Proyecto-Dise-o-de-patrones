package educatec.presentacion.modelotabla;

import educatec.modelo.dto.ReportePensionDTO;

public class PensionTM extends ModeloTablaGenerico<ReportePensionDTO> {

    public PensionTM() {
        super("Alumno", "Escala", "Monto base (S/)", "Descuento (S/)", "Pensión final (S/)");
    }

    @Override
    protected Object valor(ReportePensionDTO r, int columna) {
        return switch (columna) {
            case 0 -> r.alumno();
            case 1 -> r.escala();
            case 2 -> String.format("%.2f", r.montoBase());
            case 3 -> String.format("%.2f", r.descuento());
            default -> String.format("%.2f", r.montoFinal());
        };
    }
}
