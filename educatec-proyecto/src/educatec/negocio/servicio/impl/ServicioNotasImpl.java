package educatec.negocio.servicio.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.datos.dao.INotaDAO;
import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.dto.NotaDetalleDTO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.entidad.Nota;
import educatec.modelo.enums.EstadoMatricula;
import educatec.modelo.enums.TipoEvaluacion;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.patrones.observer.CanalNotificacion;
import educatec.negocio.patrones.observer.EventoNota;
import educatec.negocio.patrones.observer.SujetoRegistroNota;
import educatec.negocio.servicio.ServicioMatricula;
import educatec.negocio.servicio.ServicioNotas;

import java.util.List;

public class ServicioNotasImpl implements ServicioNotas {
    private final INotaDAO notaDAO;
    private final IMatriculaDAO matriculaDAO;
    private final IAlumnoDAO alumnoDAO;
    private final ICursoDAO cursoDAO;
    private final SujetoRegistroNota sujeto;
    private final CanalNotificacion canal;
    private final ServicioMatricula servicioMatricula;

    public ServicioNotasImpl(INotaDAO notaDAO, IMatriculaDAO matriculaDAO, IAlumnoDAO alumnoDAO,
                             ICursoDAO cursoDAO, SujetoRegistroNota sujeto, CanalNotificacion canal,
                             ServicioMatricula servicioMatricula) {
        this.notaDAO = notaDAO;
        this.matriculaDAO = matriculaDAO;
        this.alumnoDAO = alumnoDAO;
        this.cursoDAO = cursoDAO;
        this.sujeto = sujeto;
        this.canal = canal;
        this.servicioMatricula = servicioMatricula;
    }

    @Override
    public List<MatriculaResumenDTO> listarMatriculasPagadas() {
        return servicioMatricula.listarResumen().stream()
                .filter(m -> m.estado() == EstadoMatricula.PAGADO)
                .toList();
    }

    @Override
    public List<Curso> listarCursosDeMatricula(int idMatricula) {
        Matricula matricula = obtener(idMatricula);
        return matricula.getIdsCursos().stream()
                .map(cursoDAO::buscarPorId)
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    @Override
    public void registrarNota(int idMatricula, int idCurso, double calificacion, TipoEvaluacion tipo) {
        Matricula matricula = obtener(idMatricula);
        if (matricula.getEstado() != EstadoMatricula.PAGADO) {
            throw new NegocioException("Solo se pueden registrar notas en matrículas pagadas.");
        }
        if (!matricula.contieneCurso(idCurso)) {
            throw new NegocioException("El curso no pertenece a la matrícula seleccionada.");
        }
        if (tipo == null) {
            throw new NegocioException("Seleccione el tipo de evaluación.");
        }
        if (calificacion < Nota.NOTA_MINIMA || calificacion > Nota.NOTA_MAXIMA) {
            throw new NegocioException("La nota debe estar entre 0 y 20.");
        }
        boolean duplicada = notaDAO.listarPorMatricula(idMatricula).stream()
                .anyMatch(n -> n.getIdCurso() == idCurso && n.getEvaluacionTipo() == tipo);
        if (duplicada) {
            throw new NegocioException("Ya existe una nota de tipo " + tipo + " para este curso.");
        }
        Nota nota = notaDAO.registrar(new Nota(0, idMatricula, idCurso, calificacion, tipo));
        Alumno alumno = alumnoDAO.buscarPorId(matricula.getIdAlumno())
                .orElseThrow(() -> new NegocioException("El alumno de la matrícula no existe."));
        String nombreCurso = cursoDAO.buscarPorId(idCurso).map(Curso::getNombre).orElse("Curso " + idCurso);
        sujeto.notificar(new EventoNota(nota, alumno, nombreCurso));
    }

    @Override
    public List<NotaDetalleDTO> listarNotas(int idMatricula) {
        return notaDAO.listarPorMatricula(idMatricula).stream()
                .map(n -> new NotaDetalleDTO(n.getId(),
                        cursoDAO.buscarPorId(n.getIdCurso()).map(Curso::getNombre).orElse("-"),
                        n.getEvaluacionTipo(), n.getCalificacion(), n.esAprobada()))
                .toList();
    }

    @Override
    public double promedio(int idMatricula) {
        return notaDAO.listarPorMatricula(idMatricula).stream()
                .mapToDouble(Nota::getCalificacion)
                .average()
                .orElse(0);
    }

    @Override
    public List<String> notificaciones() {
        return canal.historial();
    }

    private Matricula obtener(int idMatricula) {
        return matriculaDAO.buscarPorId(idMatricula)
                .orElseThrow(() -> new NegocioException("La matrícula seleccionada no existe."));
    }
}
