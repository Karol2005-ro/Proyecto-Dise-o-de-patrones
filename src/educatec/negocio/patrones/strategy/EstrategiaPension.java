package educatec.negocio.patrones.strategy;

import educatec.modelo.entidad.Alumno;

public interface EstrategiaPension {
    double calcularPension(Alumno alumno, double montoBase);
}
