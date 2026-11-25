package educatec.modelo.enums;

public enum TipoServicio {
    DEPORTE("Taller de deporte"),
    IDIOMA("Taller de idiomas"),
    LABORATORIO("Laboratorio adicional");

    private final String etiqueta;

    TipoServicio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
