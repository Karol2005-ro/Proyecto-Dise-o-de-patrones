package educatec.negocio.patrones.observer;

public class ObservadorApoderado implements ObservadorNota {
    private final CanalNotificacion canal;

    public ObservadorApoderado(CanalNotificacion canal) {
        this.canal = canal;
    }

    @Override
    public void actualizar(EventoNota evento) {
        String destino = "Apoderado de " + evento.alumno().getNombreCompleto();
        String mensaje = String.format("Se registró una nota de %.1f en %s (%s).",
                evento.nota().getCalificacion(), evento.nombreCurso(), evento.nota().getEvaluacionTipo());
        if (!evento.nota().esAprobada()) {
            mensaje += " El estudiante requiere apoyo académico.";
        }
        canal.enviar(destino, mensaje);
    }
}
