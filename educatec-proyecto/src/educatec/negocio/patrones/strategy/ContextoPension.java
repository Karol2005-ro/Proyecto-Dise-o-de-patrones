package educatec.negocio.patrones.strategy;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.Escala;
import educatec.negocio.excepcion.NegocioException;

import java.util.EnumMap;
import java.util.Map;

public class ContextoPension {
    private final Map<Escala, EstrategiaPension> estrategias = new EnumMap<>(Escala.class);

    public void registrar(Escala escala, EstrategiaPension estrategia) {
        estrategias.put(escala, estrategia);
    }

    public double calcular(Alumno alumno, double montoBase) {
        if (montoBase < 0) {
            throw new NegocioException("El monto base no puede ser negativo.");
        }
        EstrategiaPension estrategia = estrategias.get(alumno.getEscalaPension());
        if (estrategia == null) {
            throw new NegocioException("No existe una estrategia de pensión para la escala " + alumno.getEscalaPension() + ".");
        }
        return estrategia.calcularPension(alumno, montoBase);
    }
}
