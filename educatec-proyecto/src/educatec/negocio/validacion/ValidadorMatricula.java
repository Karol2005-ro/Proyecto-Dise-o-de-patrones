package educatec.negocio.validacion;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;

import java.util.List;

public interface ValidadorMatricula {
    void validar(Alumno alumno, List<Curso> cursos, String periodo);
}
