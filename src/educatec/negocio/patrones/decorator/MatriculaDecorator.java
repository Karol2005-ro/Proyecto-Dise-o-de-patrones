package educatec.negocio.patrones.decorator;

public abstract class MatriculaDecorator implements ComponenteMatricula {
    protected final ComponenteMatricula componente;

    protected MatriculaDecorator(ComponenteMatricula componente) {
        this.componente = componente;
    }

    @Override
    public String getDescripcion() {
        return componente.getDescripcion();
    }

    @Override
    public double getCosto() {
        return componente.getCosto();
    }

    @Override
    public int getCreditos() {
        return componente.getCreditos();
    }
}
