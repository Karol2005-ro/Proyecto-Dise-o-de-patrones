package educatec.negocio.patrones.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ComponenteCompuesto implements ComponenteAcademico {
    private final String nombre;
    private final List<ComponenteAcademico> hijos = new ArrayList<>();

    protected ComponenteCompuesto(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ComponenteAcademico componente) {
        hijos.add(componente);
    }

    public void quitar(ComponenteAcademico componente) {
        hijos.remove(componente);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getCreditos() {
        return hijos.stream().mapToInt(ComponenteAcademico::getCreditos).sum();
    }

    @Override
    public int getHorasLectivas() {
        return hijos.stream().mapToInt(ComponenteAcademico::getHorasLectivas).sum();
    }

    @Override
    public List<ComponenteAcademico> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    @Override
    public String toString() {
        return String.format("%s  [%d créditos | %d horas]", nombre, getCreditos(), getHorasLectivas());
    }
}
