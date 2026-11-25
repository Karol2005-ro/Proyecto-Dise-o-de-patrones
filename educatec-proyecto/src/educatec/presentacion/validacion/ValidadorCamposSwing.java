package educatec.presentacion.validacion;

import educatec.negocio.excepcion.NegocioException;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

public final class ValidadorCamposSwing {
    private ValidadorCamposSwing() {
    }

    public static void exigirTexto(JTextComponent campo, String nombre) {
        if (campo.getText().trim().isEmpty()) {
            campo.requestFocusInWindow();
            throw new NegocioException("El campo " + nombre + " es obligatorio.");
        }
    }

    public static void exigirDni(JTextComponent campo) {
        if (!campo.getText().trim().matches("\\d{8}")) {
            campo.requestFocusInWindow();
            throw new NegocioException("El DNI debe contener exactamente 8 dígitos.");
        }
    }

    public static void exigirCorreoOpcional(JTextComponent campo) {
        String texto = campo.getText().trim();
        if (!texto.isEmpty() && !texto.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            campo.requestFocusInWindow();
            throw new NegocioException("El correo electrónico no tiene un formato válido.");
        }
    }

    public static void exigirNumero(JTextComponent campo, String nombre) {
        try {
            Double.parseDouble(campo.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            campo.requestFocusInWindow();
            throw new NegocioException("El campo " + nombre + " debe ser un número válido.");
        }
    }

    public static void exigirSeleccion(JComboBox<?> combo, String nombre) {
        if (combo.getSelectedItem() == null) {
            combo.requestFocusInWindow();
            throw new NegocioException("Seleccione " + nombre + ".");
        }
    }
}
