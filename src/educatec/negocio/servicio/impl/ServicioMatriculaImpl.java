package educatec.negocio.servicio.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.datos.dao.INotaDAO;
import educatec.datos.dao.IPagoDAO;
import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.entidad.Nota;
import educatec.modelo.entidad.Pago;
import educatec.modelo.enums.EstadoMatricula;
import educatec.modelo.enums.TipoServicio;
import educatec.negocio.excepcion.MatriculaException;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.patrones.adapter.ResultadoPago;
import educatec.negocio.patrones.adapter.ServicioPagoBanco;
import educatec.negocio.patrones.facade.MatriculaFacade;
import educatec.negocio.patrones.singleton.GestorCicloAcademico;
import educatec.negocio.servicio.ServicioMatricula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioMatriculaImpl implements ServicioMatricula {
    private final IAlumnoDAO alumnoDAO;
    private final ICursoDAO cursoDAO;
    private final IMatriculaDAO matriculaDAO;
    private final INotaDAO notaDAO;
    private final IPagoDAO pagoDAO;
    private final MatriculaFacade facade;
    private final ServicioPagoBanco pasarela;

    public ServicioMatriculaImpl(IAlumnoDAO alumnoDAO, ICursoDAO cursoDAO, IMatriculaDAO matriculaDAO,
                                 INotaDAO notaDAO, IPagoDAO pagoDAO, MatriculaFacade facade,
                                 ServicioPagoBanco pasarela) {
        this.alumnoDAO = alumnoDAO;
        this.cursoDAO = cursoDAO;
        this.matriculaDAO = matriculaDAO;
        this.notaDAO = notaDAO;
        this.pagoDAO = pagoDAO;
        this.facade = facade;
        this.pasarela = pasarela;
    }

    @Override
    public MatriculaResumenDTO matricular(int idAlumno, List<Integer> idsCursos, List<TipoServicio> servicios) {
        Alumno alumno = alumnoDAO.buscarPorId(idAlumno)
                .orElseThrow(() -> new MatriculaException("Seleccione un alumno válido."));
        List<Curso> cursos = new ArrayList<>();
        for (Integer idCurso : idsCursos) {
            cursos.add(cursoDAO.buscarPorId(idCurso)
                    .orElseThrow(() -> new MatriculaException("Uno de los cursos seleccionados ya no existe.")));
        }
        String periodo = GestorCicloAcademico.getInstancia().getPeriodoActivo();
        Matricula matricula = facade.matricular(alumno, cursos, servicios, periodo);
        return aResumen(matricula);
    }

    @Override
    public Pago pagar(int idMatricula, String numeroCuenta) {
        Matricula matricula = obtener(idMatricula);
        if (matricula.getEstado() != EstadoMatricula.PENDIENTE) {
            throw new NegocioException("Solo se pueden pagar matrículas en estado pendiente.");
        }
        ResultadoPago resultado = pasarela.procesarPago(numeroCuenta, matricula.getMontoTotal());
        Pago pago = new Pago(0, idMatricula, matricula.getMontoTotal(), LocalDate.now(),
                resultado.codigoOperacion(), resultado.aprobado() ? Pago.APROBADO : Pago.RECHAZADO);
        pagoDAO.registrar(pago);
        if (resultado.aprobado()) {
            matricula.setEstado(EstadoMatricula.PAGADO);
            matriculaDAO.actualizar(matricula);
        } else {
            throw new NegocioException("Pago rechazado por el banco (" + resultado.codigoOperacion() + "): " + resultado.mensaje());
        }
        return pago;
    }

    @Override
    public void anular(int idMatricula) {
        Matricula matricula = obtener(idMatricula);
        if (matricula.getEstado() == EstadoMatricula.ANULADO) {
            throw new NegocioException("La matrícula ya se encuentra anulada.");
        }
        if (matricula.getEstado() == EstadoMatricula.PAGADO) {
            throw new NegocioException("No se puede anular una matrícula que ya fue pagada.");
        }
        matricula.setEstado(EstadoMatricula.ANULADO);
        matriculaDAO.actualizar(matricula);
    }

    @Override
    public List<MatriculaResumenDTO> listarResumen() {
        return matriculaDAO.listarTodos().stream().map(this::aResumen).toList();
    }

    private Matricula obtener(int idMatricula) {
        return matriculaDAO.buscarPorId(idMatricula)
                .orElseThrow(() -> new NegocioException("La matrícula seleccionada no existe."));
    }

    private MatriculaResumenDTO aResumen(Matricula matricula) {
        Alumno alumno = alumnoDAO.buscarPorId(matricula.getIdAlumno()).orElse(null);
        int creditos = matricula.getIdsCursos().stream()
                .map(cursoDAO::buscarPorId)
                .flatMap(java.util.Optional::stream)
                .mapToInt(Curso::getCreditos)
                .sum();
        List<Nota> notas = notaDAO.listarPorMatricula(matricula.getId());
        double promedio = notas.isEmpty() ? -1 : notas.stream().mapToDouble(Nota::getCalificacion).average().orElse(-1);
        return new MatriculaResumenDTO(matricula.getId(),
                alumno == null ? "(alumno eliminado)" : alumno.getNombreCompleto(),
                alumno == null ? "-" : alumno.getDni(),
                matricula.getPeriodoAcademico(), matricula.getFechaMatricula(),
                matricula.getIdsCursos().size(), creditos, matricula.getMontoTotal(),
                matricula.getEstado(), promedio);
    }
}
