package educatec.negocio.patrones.observer;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Nota;

public record EventoNota(Nota nota, Alumno alumno, String nombreCurso) {
}
