package educatec.negocio.patrones.observer;

import java.util.ArrayList;
import java.util.List;

public class SujetoRegistroNota {
    private final List<ObservadorNota> observadores = new ArrayList<>();

    public void registrar(ObservadorNota observador) {
        observadores.add(observador);
    }

    public void quitar(ObservadorNota observador) {
        observadores.remove(observador);
    }

    public void notificar(EventoNota evento) {
        for (ObservadorNota observador : observadores) {
            observador.actualizar(evento);
        }
    }
}
