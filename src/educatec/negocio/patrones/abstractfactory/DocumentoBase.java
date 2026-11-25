package educatec.negocio.patrones.abstractfactory;

import educatec.modelo.entidad.Alumno;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class DocumentoBase implements Documento {
    private static final String LINEA = "=".repeat(56);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected abstract String titulo();

    protected abstract String modalidad();

    protected abstract String cuerpo(Alumno alumno, String periodo, String detalle);

    @Override
    public final String generar(Alumno alumno, String periodo, String detalle) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append('\n');
        sb.append("        INSTITUTO SUPERIOR EDUCATEC S.A.C.\n");
        sb.append("        ").append(titulo().toUpperCase()).append('\n');
        sb.append(LINEA).append('\n');
        sb.append(String.format("Alumno    : %s%n", alumno.getNombreCompleto()));
        sb.append(String.format("DNI       : %s%n", alumno.getDni()));
        sb.append(String.format("Modalidad : %s%n", modalidad()));
        sb.append(String.format("Periodo   : %s%n", periodo));
        sb.append(cuerpo(alumno, periodo, detalle));
        sb.append(LINEA).append('\n');
        sb.append("Emitido el ").append(LocalDate.now().format(FORMATO_FECHA)).append('\n');
        return sb.toString();
    }
}
