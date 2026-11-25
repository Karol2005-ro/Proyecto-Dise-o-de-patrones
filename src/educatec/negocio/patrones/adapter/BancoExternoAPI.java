package educatec.negocio.patrones.adapter;

import java.util.concurrent.ThreadLocalRandom;

public class BancoExternoAPI {

    public String ejecutarCargo(String cuenta, long montoEnCentimos) {
        if (cuenta == null || !cuenta.matches("\\d{10,20}")) {
            return "ERR|COD-400|Número de cuenta inválido";
        }
        if (montoEnCentimos <= 0) {
            return "ERR|COD-422|Monto inválido";
        }
        if (cuenta.startsWith("0000")) {
            return "ERR|COD-051|Fondos insuficientes";
        }
        int operacion = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "OK|BCO-" + operacion + "|Operación aprobada";
    }
}
