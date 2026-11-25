package educatec.modelo.enums;

public enum Escala {
    SOCIOECONOMICA("Escala socioeconómica"),
    BECA_EXCELENCIA("Beca por excelencia"),
    DESCUENTO_HERMANOS("Descuento por hermanos");

    private final String etiqueta;

    Escala(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
