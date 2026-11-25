package educatec.negocio.validacion;

import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.negocio.excepcion.MatriculaException;

import java.util.List;

public class ValidadorCupos implements ValidadorMatricula {
    private final IMatriculaDAO matriculaDAO;
    private final int cupoMaximo;

    public ValidadorCupos(IMatriculaDAO matriculaDAO, int cupoMaximo) {
        this.matriculaDAO = matriculaDAO;
        this.cupoMaximo = cupoMaximo;
    }

    @Override
    public void validar(Alumno alumno, List<Curso> cursos, String periodo) {
        List<Matricula> previas = matriculaDAO.listarPorAlumno(alumno.getId());
        for (Curso curso : cursos) {
            boolean yaMatriculado = previas.stream()
                    .filter(Matricula::estaVigente)
                    .filter(m -> m.getPeriodoAcademico().equals(periodo))
                    .anyMatch(m -> m.contieneCurso(curso.getId()));
            if (yaMatriculado) {
                throw new MatriculaException("El alumno ya está matriculado en " + curso.getCodigo() + " para el periodo " + periodo + ".");
            }
            if (matriculaDAO.contarPorCurso(curso.getId(), periodo) >= cupoMaximo) {
                throw new MatriculaException("No hay cupos disponibles en " + curso.getCodigo() + " (máximo " + cupoMaximo + ").");
            }
        }
    }
}
