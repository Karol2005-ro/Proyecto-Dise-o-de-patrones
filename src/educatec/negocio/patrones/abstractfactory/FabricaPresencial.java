package educatec.negocio.patrones.abstractfactory;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.ModalidadAlumno;

public class FabricaPresencial implements FabricaDocumentos {

    @Override
    public ModalidadAlumno modalidad() {
        return ModalidadAlumno.PRESENCIAL;
    }

    @Override
    public Carnet crearCarnet() {
        return new CarnetPresencial();
    }

    @Override
    public Constancia crearConstancia() {
        return new ConstanciaPresencial();
    }

    @Override
    public FichaMatricula crearFichaMatricula() {
        return new FichaMatriculaPresencial();
    }

    private static class CarnetPresencial extends DocumentoBase implements Carnet {
        @Override
        protected String titulo() {
            return "Carnet estudiantil";
        }

        @Override
        protected String modalidad() {
            return "Presencial";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            return String.format("Carrera   : %s%nAcceso    : Campus, biblioteca fisica y laboratorios%n"
                    + "Validez   : Periodo %s (presentar con DNI)%n", detalle, periodo);
        }
    }

    private static class ConstanciaPresencial extends DocumentoBase implements Constancia {
        @Override
        protected String titulo() {
            return "Constancia de estudios";
        }

        @Override
        protected String modalidad() {
            return "Presencial";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            return String.format("%nSe deja constancia que el alumno se encuentra matriculado%n"
                    + "en la carrera de %s, bajo modalidad presencial,%n"
                    + "durante el periodo academico %s.%n"
                    + "Se expide para los fines que el interesado estime conveniente.%n", detalle, periodo);
        }
    }

    private static class FichaMatriculaPresencial extends DocumentoBase implements FichaMatricula {
        @Override
        protected String titulo() {
            return "Ficha de matricula";
        }

        @Override
        protected String modalidad() {
            return "Presencial";
        }

        @Override
        protected String cuerpo(Alumno alumno, String periodo, String detalle) {
            return String.format("Horario   : Segun programacion en aula asignada%n%n%s%n", detalle);
        }
    }
}
