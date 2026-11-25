package educatec.modelo.dto;

import educatec.modelo.enums.TipoEvaluacion;

public record NotaDetalleDTO(int idNota, String curso, TipoEvaluacion evaluacion,
                             double calificacion, boolean aprobada) {
}
