package educatec.negocio.validacion;

import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.datos.dao.INotaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.entidad.Nota;
import educatec.modelo.enums.TipoEvaluacion;
import educatec.negocio.excepcion.MatriculaException;

import java.util.List;

public class ValidadorPrerrequisitos implements ValidadorMatricula {
    private final IMatriculaDAO matriculaDAO;
    private final INotaDAO notaDAO;
    private final ICursoDAO cursoDAO;

    public ValidadorPrerrequisitos(IMatriculaDAO matriculaDAO, INotaDAO notaDAO, ICursoDAO cursoDAO) {
        this.matriculaDAO = matriculaDAO;
        this.notaDAO = notaDAO;
        this.cursoDAO = cursoDAO;
    }

    @Override
    public void validar(Alumno alumno, List<Curso> cursos, String periodo) {
        for (Curso curso : cursos) {
            if (curso.tienePrerrequisito() && !aproboCurso(alumno.getId(), curso.getCodigoPrerrequisito())) {
                throw new MatriculaException("No cumple el prerrequisito del curso " + curso.getCodigo()
                        + ": debe aprobar " + curso.getCodigoPrerrequisito() + ".");
            }
        }
    }

    private boolean aproboCurso(int idAlumno, String codigoCurso) {
        for (Matricula matricula : matriculaDAO.listarPorAlumno(idAlumno)) {
            if (!matricula.estaVigente()) {
                continue;
            }
            for (Nota nota : notaDAO.listarPorMatricula(matricula.getId())) {
                if (nota.getEvaluacionTipo() == TipoEvaluacion.FINAL && nota.esAprobada()
                        && cursoDAO.buscarPorId(nota.getIdCurso())
                        .map(c -> c.getCodigo().equalsIgnoreCase(codigoCurso)).orElse(false)) {
                    return true;
                }
            }
        }
        return false;
    }
}
