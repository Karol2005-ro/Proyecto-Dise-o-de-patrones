package educatec.main;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Carrera;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Usuario;
import educatec.modelo.enums.Escala;
import educatec.modelo.enums.ModalidadAlumno;
import educatec.modelo.enums.Rol;
import educatec.modelo.enums.TipoServicio;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioMatricula;

import java.util.List;

public final class CargadorDatosPrueba {
    private CargadorDatosPrueba() {
    }

    public static List<Usuario> usuarios() {
        return List.of(
                new Usuario(1, "admin", "admin123", "Administrador del Sistema", Rol.ADMINISTRADOR),
                new Usuario(2, "docente", "docente123", "Docente Coordinador", Rol.DOCENTE));
    }

    public static void cargarCatalogos(ICarreraDAO carreraDAO, ICursoDAO cursoDAO, IAlumnoDAO alumnoDAO) {
        Carrera sistemas = carreraDAO.insertar(new Carrera(0, "ING-SYS", "Ingeniería de Sistemas", "Facultad de Ingeniería"));
        Carrera admin = carreraDAO.insertar(new Carrera(0, "ADM", "Administración de Empresas", "Facultad de Negocios"));
        Carrera conta = carreraDAO.insertar(new Carrera(0, "CON", "Contabilidad", "Facultad de Negocios"));

        cursoDAO.insertar(new Curso(0, "SIS101", "Fundamentos de Programación", 4, 6, sistemas.getId(), ""));
        cursoDAO.insertar(new Curso(0, "SIS102", "Matemática Discreta", 3, 4, sistemas.getId(), ""));
        cursoDAO.insertar(new Curso(0, "SIS103", "Base de Datos", 4, 6, sistemas.getId(), "SIS101"));
        cursoDAO.insertar(new Curso(0, "SIS201", "Diseño de Patrones", 4, 5, sistemas.getId(), "SIS103"));
        cursoDAO.insertar(new Curso(0, "SIS202", "Ingeniería de Software", 3, 4, sistemas.getId(), ""));
        cursoDAO.insertar(new Curso(0, "ADM101", "Introducción a la Administración", 3, 4, admin.getId(), ""));
        cursoDAO.insertar(new Curso(0, "ADM102", "Contabilidad General", 3, 4, admin.getId(), ""));
        cursoDAO.insertar(new Curso(0, "ADM201", "Marketing", 3, 4, admin.getId(), "ADM101"));
        cursoDAO.insertar(new Curso(0, "CON101", "Contabilidad Básica", 4, 6, conta.getId(), ""));
        cursoDAO.insertar(new Curso(0, "CON102", "Matemática Financiera", 3, 4, conta.getId(), ""));
        cursoDAO.insertar(new Curso(0, "CON201", "Costos", 4, 5, conta.getId(), "CON101"));

        alumnoDAO.insertar(new Alumno(0, "70123456", "Carlos", "Quispe Mamani", "carlos.quispe@educatec.edu.pe",
                ModalidadAlumno.PRESENCIAL, Escala.SOCIOECONOMICA, sistemas.getId()));
        alumnoDAO.insertar(new Alumno(0, "71234567", "Lucía", "Ramos Torres", "lucia.ramos@educatec.edu.pe",
                ModalidadAlumno.DISTANCIA, Escala.BECA_EXCELENCIA, sistemas.getId()));
        alumnoDAO.insertar(new Alumno(0, "72345678", "Diego", "Flores Huamán", "diego.flores@educatec.edu.pe",
                ModalidadAlumno.PRESENCIAL, Escala.DESCUENTO_HERMANOS, admin.getId()));
        alumnoDAO.insertar(new Alumno(0, "73456789", "María", "Condori Apaza", "maria.condori@educatec.edu.pe",
                ModalidadAlumno.DISTANCIA, Escala.SOCIOECONOMICA, conta.getId()));
    }

    public static void cargarMatriculaDemo(ServicioMatricula servicioMatricula) {
        try {
            var resultado = servicioMatricula.matricular(1, List.of(1, 2, 5), List.of(TipoServicio.DEPORTE));
            servicioMatricula.pagar(resultado.idMatricula(), "19400123456789");
        } catch (NegocioException ex) {
            System.err.println("No se pudo cargar la matrícula de demostración: " + ex.getMessage());
        }
    }
}
