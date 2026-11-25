package educatec.negocio.patrones.adapter;

public interface ServicioPagoBanco {
    ResultadoPago procesarPago(String numeroCuenta, double monto);
}
