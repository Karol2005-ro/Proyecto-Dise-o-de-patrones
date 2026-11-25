package educatec.negocio.servicio.impl;

import educatec.modelo.entidad.Usuario;
import educatec.negocio.excepcion.NegocioException;
import educatec.negocio.servicio.ServicioAutenticacion;

import java.util.List;

public class ServicioAutenticacionImpl implements ServicioAutenticacion {
    private final List<Usuario> usuarios;

    public ServicioAutenticacionImpl(List<Usuario> usuarios) {
        this.usuarios = List.copyOf(usuarios);
    }

    @Override
    public Usuario autenticar(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new NegocioException("Ingrese usuario y contraseña.");
        }
        return usuarios.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username.trim()) && u.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new NegocioException("Usuario o contraseña incorrectos."));
    }
}
