package educatec.datos.dao;

import educatec.modelo.entidad.Matricula;

import java.util.List;

public interface IMatriculaDAO extends IConsultaDAO<Matricula>, IEscrituraDAO<Matricula> {
    List<Matricula> listarPorAlumno(int idAlumno);

    int contarPorCurso(int idCurso, String periodoAcademico);

    boolean existeDetalleDeCurso(int idCurso);
}
