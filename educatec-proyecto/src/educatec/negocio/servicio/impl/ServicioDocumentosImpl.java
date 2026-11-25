package educatec.negocio.servicio.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Carrera;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Matricula;
import educatec.modelo.enums.ModalidadAlumno;
import educatec.modelo.enums.TipoDocumento;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.patrones.abstractfactory.FabricaDocumentos;
import educatec.negocio.patrones.singleton.GestorCicloAcademico;
import educatec.negocio.servicio.ServicioDocumentos;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ServicioDocumentosImpl implements ServicioDocumentos {
    private final IAlumnoDAO alumnoDAO;
    private final ICarreraDAO carreraDAO;
    private final ICursoDAO cursoDAO;
    private final IMatriculaDAO matriculaDAO;
    private final Map<ModalidadAlumno, FabricaDocumentos> fabricas = new EnumMap<>(ModalidadAlumno.class);

    public ServicioDocumentosImpl(IAlumnoDAO alumnoDAO, ICarreraDAO carreraDAO, ICursoDAO cursoDAO,
                                  IMatriculaDAO matriculaDAO, List<FabricaDocumentos> fabricasDisponibles) {
        this.alumnoDAO = alumnoDAO;
        this.carreraDAO = carreraDAO;
        this.cursoDAO = cursoDAO;
        this.matriculaDAO = matriculaDAO;
        for (FabricaDocumentos fabrica : fabricasDisponibles) {
            fabricas.put(fabrica.modalidad(), fabrica);
        }
    }

    @Override
    public String emitir(int idAlumno, TipoDocumento tipo) {
        if (tipo == null) {
            throw new NegocioException("Seleccione el tipo de documento.");
        }
        Alumno alumno = alumnoDAO.buscarPorId(idAlumno)
                .orElseThrow(() -> new NegocioException("Seleccione un alumno válido."));
        FabricaDocumentos fabrica = fabricas.get(alumno.getModalidad());
        if (fabrica == null) {
            throw new NegocioException("No hay documentos configurados para la modalidad " + alumno.getModalidad() + ".");
        }
        String periodo = GestorCicloAcademico.getInstancia().getPeriodoActivo();
        String carrera = carreraDAO.buscarPorId(alumno.getIdCarrera()).map(Carrera::getNombre).orElse("-");
        return switch (tipo) {
            case CARNET -> fabrica.crearCarnet().generar(alumno, periodo, carrera);
            case CONSTANCIA -> fabrica.crearConstancia().generar(alumno, periodo, carrera);
            case FICHA_MATRICULA -> fabrica.crearFichaMatricula().generar(alumno, periodo, detalleMatricula(alumno, periodo));
        };
    }

    private String detalleMatricula(Alumno alumno, String periodo) {
        Matricula matricula = matriculaDAO.listarPorAlumno(alumno.getId()).stream()
                .filter(Matricula::estaVigente)
                .filter(m -> m.getPeriodoAcademico().equals(periodo))
                .reduce((primera, segunda) -> segunda)
                .orElseThrow(() -> new NegocioException("El alumno no tiene matrícula vigente en el periodo " + periodo + "."));
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Matricula N° %d - Estado: %s%n", matricula.getId(), matricula.getEstado()));
        sb.append(String.format("%-8s %-34s %s%n", "Codigo", "Curso", "Cred."));
        int total = 0;
        for (Integer idCurso : matricula.getIdsCursos()) {
            Curso curso = cursoDAO.buscarPorId(idCurso).orElse(null);
            if (curso != null) {
                sb.append(String.format("%-8s %-34s %d%n", curso.getCodigo(), curso.getNombre(), curso.getCreditos()));
                total += curso.getCreditos();
            }
        }
        sb.append(String.format("Total de creditos: %d%n", total));
        matricula.getDetalles().stream().filter(d -> d.esServicioAdicional()).forEach(d ->
                sb.append(String.format("Servicio adicional: %s (S/ %.2f)%n", d.getTipoTallerDecorator(), d.getCostoAdicional())));
        sb.append(String.format("Monto total: S/ %.2f", matricula.getMontoTotal()));
        return sb.toString();
    }
}
