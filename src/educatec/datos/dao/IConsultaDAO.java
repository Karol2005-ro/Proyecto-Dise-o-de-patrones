package educatec.datos.dao;

import java.util.List;
import java.util.Optional;

public interface IConsultaDAO<T> {
    Optional<T> buscarPorId(int id);

    List<T> listarTodos();
}
