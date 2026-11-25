package educatec.negocio.servicio.impl;

import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Curso;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioCurso;

import java.util.List;
import java.util.Locale;

public class ServicioCursoImpl implements ServicioCurso {
    private final ICursoDAO cursoDAO;
    private final ICarreraDAO carreraDAO;
    private final IMatriculaDAO matriculaDAO;

    public ServicioCursoImpl(ICursoDAO cursoDAO, ICarreraDAO carreraDAO, IMatriculaDAO matriculaDAO) {
        this.cursoDAO = cursoDAO;
        this.carreraDAO = carreraDAO;
        this.matriculaDAO = matriculaDAO;
    }

    @Override
    public List<Curso> listar() {
        return cursoDAO.listarTodos();
    }

    @Override
    public List<Curso> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        String filtro = texto.trim().toLowerCase(Locale.ROOT);
        return cursoDAO.listarTodos().stream()
                .filter(c -> c.getCodigo().toLowerCase(Locale.ROOT).contains(filtro)
                        || c.getNombre().toLowerCase(Locale.ROOT).contains(filtro))
                .toList();
    }

    @Override
    public List<Curso> listarPorCarrera(int idCarrera) {
        return cursoDAO.listarPorCarrera(idCarrera);
    }

    @Override
    public Curso registrar(Curso curso) {
        validar(curso);
        if (cursoDAO.buscarPorCodigo(curso.getCodigo()).isPresent()) {
            throw new NegocioException("Ya existe un curso con el código " + curso.getCodigo() + ".");
        }
        return cursoDAO.insertar(curso);
    }

    @Override
    public void actualizar(Curso curso) {
        validar(curso);
        cursoDAO.buscarPorId(curso.getId())
                .orElseThrow(() -> new NegocioException("El curso que intenta actualizar no existe."));
        cursoDAO.buscarPorCodigo(curso.getCodigo()).ifPresent(existente -> {
            if (existente.getId() != curso.getId()) {
                throw new NegocioException("El código " + curso.getCodigo() + " pertenece a otro curso.");
            }
        });
        cursoDAO.actualizar(curso);
    }

    @Override
    public void eliminar(int idCurso) {
        cursoDAO.buscarPorId(idCurso)
                .orElseThrow(() -> new NegocioException("El curso que intenta eliminar no existe."));
        if (matriculaDAO.existeDetalleDeCurso(idCurso)) {
            throw new NegocioException("No se puede eliminar un curso que ya fue matriculado.");
        }
        cursoDAO.eliminar(idCurso);
    }

    private void validar(Curso curso) {
        if (curso.getCodigo() == null || curso.getCodigo().isBlank()) {
            throw new NegocioException("El código del curso es obligatorio.");
        }
        if (curso.getNombre() == null || curso.getNombre().isBlank()) {
            throw new NegocioException("El nombre del curso es obligatorio.");
        }
        if (curso.getCreditos() < 1 || curso.getCreditos() > 10) {
            throw new NegocioException("Los créditos deben estar entre 1 y 10.");
        }
        if (curso.getHorasLectivas() < 1 || curso.getHorasLectivas() > 20) {
            throw new NegocioException("Las horas lectivas deben estar entre 1 y 20.");
        }
        if (carreraDAO.buscarPorId(curso.getIdCarrera()).isEmpty()) {
            throw new NegocioException("Seleccione una carrera válida.");
        }
        if (curso.tienePrerrequisito()) {
            if (curso.getCodigoPrerrequisito().equalsIgnoreCase(curso.getCodigo())) {
                throw new NegocioException("Un curso no puede ser prerrequisito de sí mismo.");
            }
            if (cursoDAO.buscarPorCodigo(curso.getCodigoPrerrequisito()).isEmpty()) {
                throw new NegocioException("El prerrequisito " + curso.getCodigoPrerrequisito() + " no existe.");
            }
        }
    }
}
