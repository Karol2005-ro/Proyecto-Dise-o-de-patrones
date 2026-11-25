package educatec.negocio.patrones.singleton;

import educatec.negocio.excepcion.NegocioException;

import java.util.regex.Pattern;

public final class GestorCicloAcademico {
    private static final Pattern FORMATO = Pattern.compile("^\\d{4}-(I|II)$");

    private String periodoActivo = "2026-II";

    private GestorCicloAcademico() {
    }

    private static final class Contenedor {
        private static final GestorCicloAcademico INSTANCIA = new GestorCicloAcademico();
    }

    public static GestorCicloAcademico getInstancia() {
        return Contenedor.INSTANCIA;
    }

    public synchronized String getPeriodoActivo() {
        return periodoActivo;
    }

    public synchronized void configurarPeriodo(String periodo) {
        if (periodo == null || !FORMATO.matcher(periodo.trim().toUpperCase()).matches()) {
            throw new NegocioException("El periodo debe tener el formato AAAA-I o AAAA-II (ej. 2026-II).");
        }
        this.periodoActivo = periodo.trim().toUpperCase();
    }
}
