package educatec.datos.impl;

import educatec.datos.dao.ICursoDAO;
import educatec.modelo.entidad.Curso;

import java.util.List;
import java.util.Optional;

public class CursoDAOMemoria extends BaseMemoriaDAO<Curso> implements ICursoDAO {

    @Override
    protected int obtenerId(Curso entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Curso entidad, int id) {
        entidad.setId(id);
    }

    @Override
    public Optional<Curso> buscarPorCodigo(String codigo) {
        return listarTodos().stream().filter(c -> c.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    @Override
    public List<Curso> listarPorCarrera(int idCarrera) {
        return listarTodos().stream().filter(c -> c.getIdCarrera() == idCarrera).toList();
    }
}
