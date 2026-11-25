package educatec.negocio.patrones.observer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BandejaNotificaciones implements CanalNotificacion {
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final List<String> mensajes = new CopyOnWriteArrayList<>();

    @Override
    public void enviar(String destinatario, String mensaje) {
        mensajes.add(String.format("[%s] Para: %s | %s", LocalTime.now().format(HORA), destinatario, mensaje));
    }

    @Override
    public List<String> historial() {
        return List.copyOf(mensajes);
    }
}
