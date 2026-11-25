package educatec.modelo.enums;

public enum TipoDocumento {
    CARNET("Carnet estudiantil"),
    CONSTANCIA("Constancia de estudios"),
    FICHA_MATRICULA("Ficha de matrícula");

    private final String etiqueta;

    TipoDocumento(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
