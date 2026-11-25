package educatec.negocio.servicio;

import educatec.modelo.entidad.Usuario;

public interface ServicioAutenticacion {
    Usuario autenticar(String username, String password);
}
