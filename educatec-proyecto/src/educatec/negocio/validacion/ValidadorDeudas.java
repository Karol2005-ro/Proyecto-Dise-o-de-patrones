package educatec.negocio.validacion;

import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.enums.EstadoMatricula;
import educatec.negocio.excepcion.MatriculaException;

import java.util.List;

public class ValidadorDeudas implements ValidadorMatricula {
    private final IMatriculaDAO matriculaDAO;

    public ValidadorDeudas(IMatriculaDAO matriculaDAO) {
        this.matriculaDAO = matriculaDAO;
    }

    @Override
    public void validar(Alumno alumno, List<Curso> cursos, String periodo) {
        for (Matricula matricula : matriculaDAO.listarPorAlumno(alumno.getId())) {
            if (matricula.getEstado() == EstadoMatricula.PENDIENTE) {
                throw new MatriculaException("El alumno mantiene la matrícula N° " + matricula.getId() + " pendiente de pago.");
            }
        }
    }
}
