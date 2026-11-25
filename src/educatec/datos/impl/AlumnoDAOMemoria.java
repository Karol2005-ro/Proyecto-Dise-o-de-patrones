package educatec.datos.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.modelo.entidad.Alumno;

import java.util.Optional;

public class AlumnoDAOMemoria extends BaseMemoriaDAO<Alumno> implements IAlumnoDAO {

    @Override
    protected int obtenerId(Alumno entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Alumno entidad, int id) {
        entidad.setId(id);
    }

    @Override
    public Optional<Alumno> buscarPorDni(String dni) {
        return listarTodos().stream().filter(a -> a.getDni().equals(dni)).findFirst();
    }
}
