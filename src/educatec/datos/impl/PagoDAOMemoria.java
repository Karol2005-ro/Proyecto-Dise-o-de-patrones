package educatec.datos.impl;

import educatec.datos.dao.IPagoDAO;
import educatec.modelo.entidad.Pago;

import java.util.List;

public class PagoDAOMemoria extends BaseMemoriaDAO<Pago> implements IPagoDAO {

    @Override
    protected int obtenerId(Pago entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Pago entidad, int id) {
        entidad.setId(id);
    }

    @Override
    public Pago registrar(Pago pago) {
        return insertar(pago);
    }

    @Override
    public List<Pago> listarPorMatricula(int idMatricula) {
        return listarTodos().stream().filter(p -> p.getIdMatricula() == idMatricula).toList();
    }
}
