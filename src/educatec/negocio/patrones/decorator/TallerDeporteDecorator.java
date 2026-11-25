package educatec.negocio.patrones.decorator;

public class TallerDeporteDecorator extends MatriculaDecorator {
    public static final double COSTO = 80.0;
    public static final int CREDITOS = 1;

    public TallerDeporteDecorator(ComponenteMatricula componente) {
        super(componente);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Taller de deporte";
    }

    @Override
    public double getCosto() {
        return super.getCosto() + COSTO;
    }

    @Override
    public int getCreditos() {
        return super.getCreditos() + CREDITOS;
    }
}
