package educatec.negocio.servicio;

import educatec.modelo.enums.TipoDocumento;

public interface ServicioDocumentos {
    String emitir(int idAlumno, TipoDocumento tipo);
}
