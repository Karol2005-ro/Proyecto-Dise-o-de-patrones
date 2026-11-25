package educatec.negocio.controlador;

import educatec.negocio.patrones.composite.ComponenteAcademico;
import educatec.negocio.servicio.ServicioMalla;

import java.util.List;

public class MallaController {
    private final ServicioMalla servicioMalla;

    public MallaController(ServicioMalla servicioMalla) {
        this.servicioMalla = servicioMalla;
    }

    public List<ComponenteAcademico> obtenerMalla() {
        return servicioMalla.construirMalla();
    }
}
