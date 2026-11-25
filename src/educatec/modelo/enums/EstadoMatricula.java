package educatec.modelo.enums;

public enum EstadoMatricula {
    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    ANULADO("Anulado");

    private final String etiqueta;

    EstadoMatricula(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
