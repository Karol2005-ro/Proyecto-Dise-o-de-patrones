package educatec.negocio.patrones.adapter;

import educatec.negocio.excepcion.PagoException;

public class PagoBancoAdapter implements ServicioPagoBanco {
    private final BancoExternoAPI banco;

    public PagoBancoAdapter(BancoExternoAPI banco) {
        this.banco = banco;
    }

    @Override
    public ResultadoPago procesarPago(String numeroCuenta, double monto) {
        long centimos = Math.round(monto * 100);
        String respuesta = banco.ejecutarCargo(numeroCuenta, centimos);
        String[] partes = respuesta.split("\\|", 3);
        if (partes.length < 3) {
            throw new PagoException("El banco devolvió una respuesta no reconocida.");
        }
        return new ResultadoPago("OK".equals(partes[0]), partes[1], partes[2]);
    }
}
