package educatec.negocio.servicio;

import educatec.modelo.entidad.Alumno;

import java.util.List;

public interface ServicioAlumno {
    List<Alumno> listar();

    List<Alumno> buscar(String texto);

    Alumno registrar(Alumno alumno);

    void actualizar(Alumno alumno);

    void eliminar(int idAlumno);
}
