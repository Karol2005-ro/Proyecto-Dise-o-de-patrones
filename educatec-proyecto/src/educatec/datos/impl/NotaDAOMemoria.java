package educatec.datos.impl;

import educatec.datos.dao.INotaDAO;
import educatec.modelo.entidad.Nota;

import java.util.List;

public class NotaDAOMemoria extends BaseMemoriaDAO<Nota> implements INotaDAO {

    @Override
    protected int obtenerId(Nota entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Nota entidad, int id) {
        entidad.setId(id);
    }

    @Override
    public Nota registrar(Nota nota) {
        return insertar(nota);
    }

    @Override
    public List<Nota> listarPorMatricula(int idMatricula) {
        return listarTodos().stream().filter(n -> n.getIdMatricula() == idMatricula).toList();
    }
}
