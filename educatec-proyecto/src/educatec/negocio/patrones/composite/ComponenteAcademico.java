package educatec.negocio.patrones.composite;

import java.util.List;

public interface ComponenteAcademico {
    String getNombre();

    int getCreditos();

    int getHorasLectivas();

    List<ComponenteAcademico> getHijos();
}
