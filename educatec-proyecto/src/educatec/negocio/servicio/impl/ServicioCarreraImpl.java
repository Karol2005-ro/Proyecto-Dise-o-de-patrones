package educatec.negocio.servicio.impl;

import educatec.datos.dao.ICarreraDAO;
import educatec.modelo.entidad.Carrera;
import educatec.negocio.servicio.ServicioCarrera;

import java.util.List;

public class ServicioCarreraImpl implements ServicioCarrera {
    private final ICarreraDAO carreraDAO;

    public ServicioCarreraImpl(ICarreraDAO carreraDAO) {
        this.carreraDAO = carreraDAO;
    }

    @Override
    public List<Carrera> listar() {
        return carreraDAO.listarTodos();
    }
}
