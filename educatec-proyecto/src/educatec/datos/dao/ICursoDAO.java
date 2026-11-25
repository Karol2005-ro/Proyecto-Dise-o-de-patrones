package educatec.datos.dao;

import educatec.modelo.entidad.Curso;

import java.util.List;
import java.util.Optional;

public interface ICursoDAO extends IConsultaDAO<Curso>, IEscrituraDAO<Curso> {
    Optional<Curso> buscarPorCodigo(String codigo);

    List<Curso> listarPorCarrera(int idCarrera);
}
