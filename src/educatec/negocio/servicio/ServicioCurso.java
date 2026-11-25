package educatec.negocio.servicio;

import educatec.modelo.entidad.Curso;

import java.util.List;

public interface ServicioCurso {
    List<Curso> listar();

    List<Curso> buscar(String texto);

    List<Curso> listarPorCarrera(int idCarrera);

    Curso registrar(Curso curso);

    void actualizar(Curso curso);

    void eliminar(int idCurso);
}
