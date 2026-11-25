package educatec.presentacion.panel;

import educatec.negocio.excepcion.NegocioException;

import javax.swing.JOptionPane;
import java.awt.Component;

public final class Mensajes {
    private Mensajes() {
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "EducaTec", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "EducaTec - Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component padre, String mensaje) {
        return JOptionPane.showConfirmDialog(padre, mensaje, "EducaTec - Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public static void ejecutar(Component padre, Runnable accion) {
        try {
            accion.run();
        } catch (NegocioException ex) {
            error(padre, ex.getMessage());
        } catch (RuntimeException ex) {
            error(padre, "Ocurrió un error inesperado: " + ex.getMessage());
        }
    }
}
