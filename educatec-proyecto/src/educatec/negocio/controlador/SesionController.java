package educatec.negocio.controlador;

import educatec.modelo.entidad.Usuario;
import educatec.negocio.patrones.singleton.GestorCicloAcademico;
import educatec.negocio.patrones.singleton.GestorSesion;
import educatec.negocio.servicio.ServicioAutenticacion;

public class SesionController {
    private final ServicioAutenticacion autenticacion;

    public SesionController(ServicioAutenticacion autenticacion) {
        this.autenticacion = autenticacion;
    }

    public Usuario iniciarSesion(String username, char[] password) {
        Usuario usuario = autenticacion.autenticar(username, new String(password));
        GestorSesion.getInstancia().iniciar(usuario);
        return usuario;
    }

    public void cerrarSesion() {
        GestorSesion.getInstancia().cerrar();
    }

    public Usuario getUsuarioActual() {
        return GestorSesion.getInstancia().getUsuarioActual();
    }

    public String getPeriodoActivo() {
        return GestorCicloAcademico.getInstancia().getPeriodoActivo();
    }

    public void configurarPeriodo(String periodo) {
        GestorCicloAcademico.getInstancia().configurarPeriodo(periodo);
    }
}
