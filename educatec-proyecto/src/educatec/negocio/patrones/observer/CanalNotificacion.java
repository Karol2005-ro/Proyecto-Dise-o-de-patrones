package educatec.negocio.patrones.observer;

import java.util.List;

public interface CanalNotificacion {
    void enviar(String destinatario, String mensaje);

    List<String> historial();
}
