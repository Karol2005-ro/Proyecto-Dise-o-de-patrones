package educatec.datos.impl;

import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.DetalleMatricula;
import educatec.modelo.entidad.Matricula;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MatriculaDAOMemoria extends BaseMemoriaDAO<Matricula> implements IMatriculaDAO {
    private final AtomicInteger secuenciaDetalle = new AtomicInteger(0);

    @Override
    protected int obtenerId(Matricula entidad) {
        return entidad.getId();
    }

    @Override
    protected void asignarId(Matricula entidad, int id) {
        entidad.setId(id);
    }

    @Override
    public synchronized Matricula insertar(Matricula entidad) {
        Matricula guardada = super.insertar(entidad);
        for (DetalleMatricula detalle : guardada.getDetalles()) {
            detalle.setId(secuenciaDetalle.incrementAndGet());
            detalle.setIdMatricula(guardada.getId());
        }
        return guardada;
    }

    @Override
    public List<Matricula> listarPorAlumno(int idAlumno) {
        return listarTodos().stream().filter(m -> m.getIdAlumno() == idAlumno).toList();
    }

    @Override
    public int contarPorCurso(int idCurso, String periodoAcademico) {
        return (int) listarTodos().stream()
                .filter(Matricula::estaVigente)
                .filter(m -> m.getPeriodoAcademico().equals(periodoAcademico))
                .filter(m -> m.contieneCurso(idCurso))
                .count();
    }

    @Override
    public boolean existeDetalleDeCurso(int idCurso) {
        return listarTodos().stream().anyMatch(m -> m.contieneCurso(idCurso));
    }
}
