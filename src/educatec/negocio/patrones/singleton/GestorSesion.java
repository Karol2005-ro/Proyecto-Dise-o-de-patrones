package educatec.negocio.patrones.singleton;

import educatec.modelo.entidad.Usuario;
import educatec.modelo.enums.Rol;

public final class GestorSesion {
    private Usuario usuarioActual;

    private GestorSesion() {
    }

    private static final class Contenedor {
        private static final GestorSesion INSTANCIA = new GestorSesion();
    }

    public static GestorSesion getInstancia() {
        return Contenedor.INSTANCIA;
    }

    public synchronized void iniciar(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public synchronized void cerrar() {
        this.usuarioActual = null;
    }

    public synchronized Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public synchronized boolean estaAutenticado() {
        return usuarioActual != null;
    }

    public synchronized boolean esAdministrador() {
        return usuarioActual != null && usuarioActual.getRol() == Rol.ADMINISTRADOR;
    }
}
