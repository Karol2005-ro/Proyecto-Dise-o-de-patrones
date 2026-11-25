package educatec.negocio.patrones.strategy;

import educatec.modelo.entidad.Alumno;

public class EstrategiaDescuentoHermanos implements EstrategiaPension {
    private static final double PORCENTAJE_DESCUENTO = 0.15;

    @Override
    public double calcularPension(Alumno alumno, double montoBase) {
        return Math.round(montoBase * (1 - PORCENTAJE_DESCUENTO) * 100.0) / 100.0;
    }
}
