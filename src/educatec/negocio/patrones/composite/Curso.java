package educatec.negocio.patrones.composite;

import java.util.Collections;
import java.util.List;

public class Curso implements ComponenteAcademico {
    private final String nombre;
    private final int creditos;
    private final int horasLectivas;

    public Curso(String nombre, int creditos, int horasLectivas) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasLectivas = horasLectivas;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getCreditos() {
        return creditos;
    }

    @Override
    public int getHorasLectivas() {
        return horasLectivas;
    }

    @Override
    public List<ComponenteAcademico> getHijos() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return String.format("%s  [%d créditos | %d horas]", nombre, creditos, horasLectivas);
    }
}
