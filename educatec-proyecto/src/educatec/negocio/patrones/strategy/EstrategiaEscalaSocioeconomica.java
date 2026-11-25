package educatec.negocio.patrones.strategy;

import educatec.modelo.entidad.Alumno;

public class EstrategiaEscalaSocioeconomica implements EstrategiaPension {
    private static final double PORCENTAJE_DESCUENTO = 0.10;

    @Override
    public double calcularPension(Alumno alumno, double montoBase) {
        return Math.round(montoBase * (1 - PORCENTAJE_DESCUENTO) * 100.0) / 100.0;
    }
}
