package educatec.negocio.patrones.decorator;

public class TallerIdiomaDecorator extends MatriculaDecorator {
    public static final double COSTO = 150.0;
    public static final int CREDITOS = 2;

    public TallerIdiomaDecorator(ComponenteMatricula componente) {
        super(componente);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Taller de idiomas";
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
