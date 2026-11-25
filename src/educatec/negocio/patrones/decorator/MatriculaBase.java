package educatec.negocio.patrones.decorator;

import educatec.modelo.entidad.Curso;

import java.util.List;
import java.util.stream.Collectors;

public class MatriculaBase implements ComponenteMatricula {
    private final List<Curso> cursos;
    private final double costoPorCredito;

    public MatriculaBase(List<Curso> cursos, double costoPorCredito) {
        this.cursos = List.copyOf(cursos);
        this.costoPorCredito = costoPorCredito;
    }

    @Override
    public String getDescripcion() {
        return cursos.stream().map(Curso::getCodigo).collect(Collectors.joining(", "));
    }

    @Override
    public double getCosto() {
        return getCreditos() * costoPorCredito;
    }

    @Override
    public int getCreditos() {
        return cursos.stream().mapToInt(Curso::getCreditos).sum();
    }
}
