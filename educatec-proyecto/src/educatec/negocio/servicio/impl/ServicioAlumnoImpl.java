package educatec.negocio.servicio.impl;

import educatec.datos.dao.IAlumnoDAO;
import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.IMatriculaDAO;
import educatec.modelo.entidad.Alumno;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioAlumno;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ServicioAlumnoImpl implements ServicioAlumno {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final IAlumnoDAO alumnoDAO;
    private final ICarreraDAO carreraDAO;
    private final IMatriculaDAO matriculaDAO;

    public ServicioAlumnoImpl(IAlumnoDAO alumnoDAO, ICarreraDAO carreraDAO, IMatriculaDAO matriculaDAO) {
        this.alumnoDAO = alumnoDAO;
        this.carreraDAO = carreraDAO;
        this.matriculaDAO = matriculaDAO;
    }

    @Override
    public List<Alumno> listar() {
        return alumnoDAO.listarTodos();
    }

    @Override
    public List<Alumno> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        String filtro = texto.trim().toLowerCase(Locale.ROOT);
        return alumnoDAO.listarTodos().stream()
                .filter(a -> a.getDni().contains(filtro)
                        || a.getNombreCompleto().toLowerCase(Locale.ROOT).contains(filtro))
                .toList();
    }

    @Override
    public Alumno registrar(Alumno alumno) {
        validar(alumno);
        if (alumnoDAO.buscarPorDni(alumno.getDni()).isPresent()) {
            throw new NegocioException("Ya existe un alumno registrado con el DNI " + alumno.getDni() + ".");
        }
        return alumnoDAO.insertar(alumno);
    }

    @Override
    public void actualizar(Alumno alumno) {
        validar(alumno);
        alumnoDAO.buscarPorId(alumno.getId())
                .orElseThrow(() -> new NegocioException("El alumno que intenta actualizar no existe."));
        alumnoDAO.buscarPorDni(alumno.getDni()).ifPresent(existente -> {
            if (existente.getId() != alumno.getId()) {
                throw new NegocioException("El DNI " + alumno.getDni() + " pertenece a otro alumno.");
            }
        });
        alumnoDAO.actualizar(alumno);
    }

    @Override
    public void eliminar(int idAlumno) {
        alumnoDAO.buscarPorId(idAlumno)
                .orElseThrow(() -> new NegocioException("El alumno que intenta eliminar no existe."));
        if (!matriculaDAO.listarPorAlumno(idAlumno).isEmpty()) {
            throw new NegocioException("No se puede eliminar un alumno que tiene matrículas registradas.");
        }
        alumnoDAO.eliminar(idAlumno);
    }

    private void validar(Alumno alumno) {
        if (alumno.getDni() == null || !alumno.getDni().matches("\\d{8}")) {
            throw new NegocioException("El DNI debe tener exactamente 8 dígitos.");
        }
        if (alumno.getNombres() == null || alumno.getNombres().isBlank()) {
            throw new NegocioException("Los nombres son obligatorios.");
        }
        if (alumno.getApellidos() == null || alumno.getApellidos().isBlank()) {
            throw new NegocioException("Los apellidos son obligatorios.");
        }
        if (alumno.getEmail() != null && !alumno.getEmail().isBlank() && !EMAIL.matcher(alumno.getEmail().trim()).matches()) {
            throw new NegocioException("El correo electrónico no tiene un formato válido.");
        }
        if (alumno.getModalidad() == null) {
            throw new NegocioException("Seleccione la modalidad del alumno.");
        }
        if (alumno.getEscalaPension() == null) {
            throw new NegocioException("Seleccione la escala de pensión del alumno.");
        }
        if (carreraDAO.buscarPorId(alumno.getIdCarrera()).isEmpty()) {
            throw new NegocioException("Seleccione una carrera válida.");
        }
    }
}
