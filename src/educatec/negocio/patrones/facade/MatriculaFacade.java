package educatec.negocio.patrones.facade;

import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.enums.EstadoMatricula;
import educatec.modelo.enums.TipoServicio;
import educatec.negocio.excepcion.MatriculaException;
import educatec.negocio.patrones.decorator.ComponenteMatricula;
import educatec.negocio.patrones.decorator.LaboratorioDecorator;
import educatec.negocio.patrones.decorator.MatriculaBase;
import educatec.negocio.patrones.decorator.TallerDeporteDecorator;
import educatec.negocio.patrones.decorator.TallerIdiomaDecorator;
import educatec.negocio.patrones.strategy.ContextoPension;
import educatec.negocio.validacion.ValidadorMatricula;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

public class MatriculaFacade {
    public static final double COSTO_POR_CREDITO = 60.0;
    public static final int MAX_CREDITOS = 26;

    private final ValidadorMatricula validadorPrerrequisitos;
    private final ValidadorMatricula validadorCupos;
    private final ValidadorMatricula validadorDeudas;
    private final ContextoPension contextoPension;
    private final IMatriculaDAO matriculaDAO;

    public MatriculaFacade(ValidadorMatricula validadorPrerrequisitos, ValidadorMatricula validadorCupos,
                           ValidadorMatricula validadorDeudas, ContextoPension contextoPension,
                           IMatriculaDAO matriculaDAO) {
        this.validadorPrerrequisitos = validadorPrerrequisitos;
        this.validadorCupos = validadorCupos;
        this.validadorDeudas = validadorDeudas;
        this.contextoPension = contextoPension;
        this.matriculaDAO = matriculaDAO;
    }

    public Matricula matricular(Alumno alumno, List<Curso> cursos, List<TipoServicio> servicios, String periodo) {
        if (cursos == null || cursos.isEmpty()) {
            throw new MatriculaException("Seleccione al menos un curso para matricular.");
        }
        validarPrerrequisitos(alumno, cursos, periodo);
        reservarCupo(alumno, cursos, periodo);
        return liquidar(alumno, cursos, servicios == null ? List.of() : servicios, periodo);
    }

    private void validarPrerrequisitos(Alumno alumno, List<Curso> cursos, String periodo) {
        validadorPrerrequisitos.validar(alumno, cursos, periodo);
    }

    private void reservarCupo(Alumno alumno, List<Curso> cursos, String periodo) {
        validadorDeudas.validar(alumno, cursos, periodo);
        validadorCupos.validar(alumno, cursos, periodo);
    }

    private Matricula liquidar(Alumno alumno, List<Curso> cursos, List<TipoServicio> servicios, String periodo) {
        Matricula matricula = new Matricula(0, alumno.getId(), periodo, LocalDate.now(), 0, EstadoMatricula.PENDIENTE);
        for (Curso curso : cursos) {
            matricula.agregarCurso(curso.getId());
        }
        ComponenteMatricula actual = new MatriculaBase(cursos, COSTO_POR_CREDITO);
        for (TipoServicio servicio : new LinkedHashSet<>(servicios)) {
            ComponenteMatricula decorado = decorar(actual, servicio);
            double costoServicio = decorado.getCosto() - actual.getCosto();
            matricula.agregarServicio(cursos.get(0).getId(), costoServicio, decorado.getClass().getSimpleName());
            actual = decorado;
        }
        if (actual.getCreditos() > MAX_CREDITOS) {
            throw new MatriculaException("La matrícula excede el máximo de " + MAX_CREDITOS + " créditos (" + actual.getCreditos() + ").");
        }
        double total = contextoPension.calcular(alumno, actual.getCosto());
        matricula.setMontoTotal(Math.round(total * 100.0) / 100.0);
        return matriculaDAO.insertar(matricula);
    }

    private ComponenteMatricula decorar(ComponenteMatricula componente, TipoServicio servicio) {
        return switch (servicio) {
            case DEPORTE -> new TallerDeporteDecorator(componente);
            case IDIOMA -> new TallerIdiomaDecorator(componente);
            case LABORATORIO -> new LaboratorioDecorator(componente);
        };
    }
}
