package educatec.modelo.enums;

public enum ModalidadAlumno {
    PRESENCIAL("Presencial"),
    DISTANCIA("A distancia");

    private final String etiqueta;

    ModalidadAlumno(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
