package educatec.datos.impl;

import educatec.datos.dao.ICarreraDAO;
import educatec.modelo.entidad.Carrera;

public class CarreraDAOMemoria extends BaseMemoriaDAO<Carrera> implements ICarreraDAO {

    @Override
    protected int obtenerId(Carrera entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Carrera entidad, int id) {
        entidad.setId(id);
    }
}
