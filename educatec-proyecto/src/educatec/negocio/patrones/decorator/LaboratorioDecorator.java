package educatec.negocio.patrones.decorator;

public class LaboratorioDecorator extends MatriculaDecorator {
    public static final double COSTO = 120.0;
    public static final int CREDITOS = 1;

    public LaboratorioDecorator(ComponenteMatricula componente) {
        super(componente);
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Laboratorio adicional";
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
