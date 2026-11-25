package educatec.modelo.enums;

public enum TipoEvaluacion {
    PARCIAL("Parcial"),
    CONTINUA("Continua"),
    FINAL("Final");

    private final String etiqueta;

    TipoEvaluacion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
