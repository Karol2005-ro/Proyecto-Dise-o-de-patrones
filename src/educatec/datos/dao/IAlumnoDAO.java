package educatec.datos.dao;

import educatec.modelo.entidad.Alumno;

import java.util.Optional;

public interface IAlumnoDAO extends IConsultaDAO<Alumno>, IEscrituraDAO<Alumno> {
    Optional<Alumno> buscarPorDni(String dni);
}
