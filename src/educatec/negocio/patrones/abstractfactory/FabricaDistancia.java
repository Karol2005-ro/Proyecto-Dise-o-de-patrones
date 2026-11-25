package educatec.negocio.patrones.abstractfactory;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.ModalidadAlumno;

public class FabricaDistancia implements FabricaDocumentos {

    @Override
    public ModalidadAlumno modalidad() {
        return ModalidadAlumno.DISTANCIA;
    }

    @Override
    public Carnet crearCarnet() {
        return new CarnetDistancia();
    }

    @Override
    public Constancia crearConstancia() {
        return new ConstanciaDistancia();
    }

    @Override
    public FichaMatricula crearFichaMatricula() {
        return new FichaMatriculaDistancia();
    }

    private static class CarnetDistancia extends DocumentoBase implements Carnet {
        @Override
        protected String titulo() {
            return "Carnet digital";
        }

        @Override
        protected String modalidad() {
            return "A distancia";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            String usuario = alumno.getDni() + "@educatec.edu.pe";
            return String.format("Carrera   : %s%nAcceso    : Aula virtual y biblioteca digital%n"
                    + "Usuario   : %s%nCodigo QR : EDU-%s-%d%n", detalle, usuario, periodo, alumno.getId());
        }
    }

    private static class ConstanciaDistancia extends DocumentoBase implements Constancia {
        @Override
        protected String titulo() {
            return "Constancia de estudios (virtual)";
        }

        @Override
        protected String modalidad() {
            return "A distancia";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            return String.format("%nSe deja constancia que el alumno cursa la carrera de %s%n"
                    + "bajo modalidad a distancia en el periodo %s.%n"
                    + "Documento digital verificable mediante codigo EDU-%s-%d.%n",
                    detalle, periodo, periodo, alumno.getId());
        }
    }

    private static class FichaMatriculaDistancia extends DocumentoBase implements FichaMatricula {
        @Override
        protected String titulo() {
            return "Ficha de matricula (virtual)";
        }

        @Override
        protected String modalidad() {
            return "A distancia";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            return String.format("Sesiones  : Clases virtuales sincronicas y asincronicas%n%n%s%n", detalle);
        }
    }
}
