package educatec.datos.dao;

import educatec.modelo.entidad.Pago;

import java.util.List;

public interface IPagoDAO extends IConsultaDAO<Pago> {
    Pago registrar(Pago pago);

    List<Pago> listarPorMatricula(int idMatricula);
}
