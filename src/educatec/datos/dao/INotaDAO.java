package educatec.datos.dao;

import educatec.modelo.entidad.Nota;

import java.util.List;

public interface INotaDAO extends IConsultaDAO<Nota> {
    Nota registrar(Nota nota);

    List<Nota> listarPorMatricula(int idMatricula);
}
