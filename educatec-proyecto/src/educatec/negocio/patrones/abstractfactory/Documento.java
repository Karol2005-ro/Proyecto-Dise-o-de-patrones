package educatec.negocio.patrones.abstractfactory;

import educatec.modelo.entidad.Alumno;

public interface Documento {
    String generar(Alumno alumno, String periodo, String detalle);
}
