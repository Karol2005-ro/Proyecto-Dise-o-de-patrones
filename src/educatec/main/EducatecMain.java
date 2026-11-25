package educatec.main;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.datos.dao.INotaDAO;
import educatec.datos.dao.IPagoDAO;
import educatec.datos.impl.AlumnoDAOMemoria;
import educatec.datos.impl.CarreraDAOMemoria;
import educatec.datos.impl.CursoDAOMemoria;
import educatec.datos.impl.MatriculaDAOMemoria;
import educatec.datos.impl.NotaDAOMemoria;
import educatec.datos.impl.PagoDAOMemoria;
import educatec.modelo.enums.Escala;
import educatec.negocio.controlador.AlumnoController;
import educatec.negocio.controlador.Controladores;
import educatec.negocio.controlador.CursoController;
import educatec.negocio.controlador.DocumentosController;
import educatec.negocio.controlador.MallaController;
import educatec.negocio.controlador.MatriculaController;
import educatec.negocio.controlador.NotasController;
import educatec.negocio.controlador.PensionController;
import educatec.negocio.controlador.SesionController;
import educatec.negocio.patrones.abstractfactory.FabricaDistancia;
import educatec.negocio.patrones.abstractfactory.FabricaPresencial;
import educatec.negocio.patrones.adapter.BancoExternoAPI;
import educatec.negocio.patrones.adapter.PagoBancoAdapter;
import educatec.negocio.patrones.adapter.ServicioPagoBanco;
import educatec.negocio.patrones.facade.MatriculaFacade;
import educatec.negocio.patrones.observer.BandejaNotificaciones;
import educatec.negocio.patrones.observer.ObservadorAlumno;
import educatec.negocio.patrones.observer.ObservadorApoderado;
import educatec.negocio.patrones.observer.SujetoRegistroNota;
import educatec.negocio.patrones.strategy.ContextoPension;
import educatec.negocio.patrones.strategy.EstrategiaBecaExcelencia;
import educatec.negocio.patrones.strategy.EstrategiaDescuentoHermanos;
import educatec.negocio.patrones.strategy.EstrategiaEscalaSocioeconomica;
import educatec.negocio.servicio.ServicioAlumno;
import educatec.negocio.servicio.ServicioAutenticacion;
import educatec.negocio.servicio.ServicioCarrera;
import educatec.negocio.servicio.ServicioCurso;
import educatec.negocio.servicio.ServicioDocumentos;
import educatec.negocio.servicio.ServicioMalla;
import educatec.negocio.servicio.ServicioMatricula;
import educatec.negocio.servicio.ServicioNotas;
import educatec.negocio.servicio.ServicioPension;
import educatec.negocio.servicio.impl.ServicioAlumnoImpl;
import educatec.negocio.servicio.impl.ServicioAutenticacionImpl;
import educatec.negocio.servicio.impl.ServicioCarreraImpl;
import educatec.negocio.servicio.impl.ServicioCursoImpl;
import educatec.negocio.servicio.impl.ServicioDocumentosImpl;
import educatec.negocio.servicio.impl.ServicioMallaImpl;
import educatec.negocio.servicio.impl.ServicioMatriculaImpl;
import educatec.negocio.servicio.impl.ServicioNotasImpl;
import educatec.negocio.servicio.impl.ServicioPensionImpl;
import educatec.negocio.validacion.ValidadorCupos;
import educatec.negocio.validacion.ValidadorDeudas;
import educatec.negocio.validacion.ValidadorPrerrequisitos;
import educatec.presentacion.frame.LoginFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;
import java.util.List;

public final class EducatecMain {
    private static final int CUPO_MAXIMO_POR_CURSO = 30;

    private EducatecMain() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                configurarApariencia();
                Controladores controladores = ensamblar();
                new LoginFrame(controladores).setVisible(true);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo iniciar la aplicación: " + ex.getMessage(),
                        "EducaTec", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void configurarApariencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorada) {
            System.err.println("Se usará la apariencia por defecto.");
        }
        Font fuente = new Font("SansSerif", Font.PLAIN, 13);
        for (Object clave : UIManager.getDefaults().keySet()) {
            if (clave.toString().endsWith(".font")) {
                UIManager.put(clave, fuente);
            }
        }
    }

    private static Controladores ensamblar() {
        IAlumnoDAO alumnoDAO = new AlumnoDAOMemoria();
        ICarreraDAO carreraDAO = new CarreraDAOMemoria();
        ICursoDAO cursoDAO = new CursoDAOMemoria();
        IMatriculaDAO matriculaDAO = new MatriculaDAOMemoria();
        INotaDAO notaDAO = new NotaDAOMemoria();
        IPagoDAO pagoDAO = new PagoDAOMemoria();
        CargadorDatosPrueba.cargarCatalogos(carreraDAO, cursoDAO, alumnoDAO);

        ContextoPension contextoPension = new ContextoPension();
        contextoPension.registrar(Escala.SOCIOECONOMICA, new EstrategiaEscalaSocioeconomica());
        contextoPension.registrar(Escala.BECA_EXCELENCIA, new EstrategiaBecaExcelencia());
        contextoPension.registrar(Escala.DESCUENTO_HERMANOS, new EstrategiaDescuentoHermanos());

        MatriculaFacade facade = new MatriculaFacade(
                new ValidadorPrerrequisitos(matriculaDAO, notaDAO, cursoDAO),
                new ValidadorCupos(matriculaDAO, CUPO_MAXIMO_POR_CURSO),
                new ValidadorDeudas(matriculaDAO),
                contextoPension, matriculaDAO);

        ServicioPagoBanco pasarela = new PagoBancoAdapter(new BancoExternoAPI());

        BandejaNotificaciones bandeja = new BandejaNotificaciones();
        SujetoRegistroNota sujeto = new SujetoRegistroNota();
        sujeto.registrar(new ObservadorAlumno(bandeja));
        sujeto.registrar(new ObservadorApoderado(bandeja));

        ServicioAutenticacion servicioAutenticacion = new ServicioAutenticacionImpl(CargadorDatosPrueba.usuarios());
        ServicioCarrera servicioCarrera = new ServicioCarreraImpl(carreraDAO);
        ServicioAlumno servicioAlumno = new ServicioAlumnoImpl(alumnoDAO, carreraDAO, matriculaDAO);
        ServicioCurso servicioCurso = new ServicioCursoImpl(cursoDAO, carreraDAO, matriculaDAO);
        ServicioMatricula servicioMatricula = new ServicioMatriculaImpl(alumnoDAO, cursoDAO, matriculaDAO,
                notaDAO, pagoDAO, facade, pasarela);
        ServicioNotas servicioNotas = new ServicioNotasImpl(notaDAO, matriculaDAO, alumnoDAO, cursoDAO,
                sujeto, bandeja, servicioMatricula);
        ServicioPension servicioPension = new ServicioPensionImpl(alumnoDAO, contextoPension);
        ServicioDocumentos servicioDocumentos = new ServicioDocumentosImpl(alumnoDAO, carreraDAO, cursoDAO,
                matriculaDAO, List.of(new FabricaPresencial(), new FabricaDistancia()));
        ServicioMalla servicioMalla = new ServicioMallaImpl(carreraDAO, cursoDAO);

        CargadorDatosPrueba.cargarMatriculaDemo(servicioMatricula);

        return new Controladores(
                new SesionController(servicioAutenticacion),
                new AlumnoController(servicioAlumno, servicioCarrera),
                new CursoController(servicioCurso, servicioCarrera),
                new MatriculaController(servicioMatricula, servicioAlumno, servicioCurso),
                new NotasController(servicioNotas),
                new PensionController(servicioPension),
                new DocumentosController(servicioDocumentos, servicioAlumno),
                new MallaController(servicioMalla));
    }
}
