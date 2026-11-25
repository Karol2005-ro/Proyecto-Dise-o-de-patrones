package educatec.negocio.patrones.observer;

public class ObservadorAlumno implements ObservadorNota {
    private final CanalNotificacion canal;

    public ObservadorAlumno(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void actualizar(EventoNota evento) {
        String destino = evento.alumno().getNombreCompleto();
        String correo = evento.alumno().getEmail();
        if (correo != null && !correo.isBlank()) {
            destino = destino + " <" + correo + ">";
        }
        String mensaje = String.format("Nueva calificación en %s (%s): %.1f.",
                evento.nombreCurso(), evento.nota().getEvaluacionTipo(), evento.nota().getCalificacion());
        if (!evento.nota().esAprobada()) {
            mensaje += " ALERTA DE RENDIMIENTO: nota desaprobatoria.";
        }
        canal.enviar(destino, mensaje);
    }
}
