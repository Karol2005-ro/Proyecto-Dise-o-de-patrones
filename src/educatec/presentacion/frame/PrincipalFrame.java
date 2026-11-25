package educatec.presentacion.frame;

import educatec.negocio.controlador.Controladores;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PrincipalFrame extends JFrame {
    private final Controladores controladores;
    private final JDesktopPane escritorio = new JDesktopPane();
    private final JLabel lblEstado = new JLabel();
    private final Map<Class<?>, JInternalFrame> abiertos = new HashMap<>();
    private int desplazamiento = 0;

    public PrincipalFrame(Controladores controladores) {
        super("EducaTec - Sistema de Gestión Académica");
        this.controladores = controladores;
        construir();
        actualizarEstado();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 680));
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void construir() {
        escritorio.setBackground(new Color(0xDCE4EE));
        setJMenuBar(crearMenu());
        lblEstado.setFont(Estilos.FUENTE);
        lblEstado.setForeground(Color.WHITE);
        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBackground(Estilos.PRIMARIO);
        barraEstado.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        barraEstado.add(lblEstado, BorderLayout.WEST);
        getContentPane().add(escritorio, BorderLayout.CENTER);
        getContentPane().add(barraEstado, BorderLayout.SOUTH);
    }

    private JMenuBar crearMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu sistema = new JMenu("Sistema");
        sistema.add(item("Configurar ciclo académico", this::configurarCiclo));
        sistema.add(item("Cerrar sesión", this::cerrarSesion));
        sistema.addSeparator();
        sistema.add(item("Salir", () -> System.exit(0)));

        JMenu academico = new JMenu("Académico");
        academico.add(item("Alumnos", () -> abrir(AlumnosInternalFrame.class,
                () -> new AlumnosInternalFrame(controladores.alumno))));
        academico.add(item("Cursos", () -> abrir(CursosInternalFrame.class,
                () -> new CursosInternalFrame(controladores.curso))));
        academico.add(item("Malla académica", () -> abrir(MallaInternalFrame.class,
                () -> new MallaInternalFrame(controladores.malla))));

        JMenu gestion = new JMenu("Gestión");
        gestion.add(item("Matrícula", () -> abrir(MatriculaInternalFrame.class,
                () -> new MatriculaInternalFrame(controladores.matricula, controladores.sesion))));
        gestion.add(item("Notas y notificaciones", () -> abrir(NotasInternalFrame.class,
                () -> new NotasInternalFrame(controladores.notas))));
        gestion.add(item("Pensiones", () -> abrir(PensionesInternalFrame.class,
                () -> new PensionesInternalFrame(controladores.pension))));

        JMenu documentos = new JMenu("Documentos");
        documentos.add(item("Emitir documentos y carnets", () -> abrir(DocumentosInternalFrame.class,
                () -> new DocumentosInternalFrame(controladores.documentos))));

        JMenu reportes = new JMenu("Reportes");
        reportes.add(item("Matrículas y rendimiento", () -> abrir(ReportesInternalFrame.class,
                () -> new ReportesInternalFrame(controladores.matricula, controladores.notas))));

        barra.add(sistema);
        barra.add(academico);
        barra.add(gestion);
        barra.add(documentos);
        barra.add(reportes);
        return barra;
    }

    private JMenuItem item(String texto, Runnable accion) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(Estilos.FUENTE);
        item.addActionListener(e -> Mensajes.ejecutar(this, accion));
        return item;
    }

    private void abrir(Class<?> tipo, Supplier<JInternalFrame> fabrica) {
        JInternalFrame existente = abiertos.get(tipo);
        if (existente != null && !existente.isClosed()) {
            existente.toFront();
            try {
                existente.setIcon(false);
                existente.setSelected(true);
            } catch (PropertyVetoException ignorada) {
                existente.toFront();
            }
            return;
        }
        JInternalFrame nuevo = fabrica.get();
        desplazamiento = (desplazamiento + 28) % 140;
        nuevo.setLocation(20 + desplazamiento, 20 + desplazamiento);
        escritorio.add(nuevo);
        nuevo.setVisible(true);
        abiertos.put(tipo, nuevo);
        try {
            nuevo.setSelected(true);
        } catch (PropertyVetoException ignorada) {
            nuevo.toFront();
        }
    }

    private void configurarCiclo() {
        Object entrada = JOptionPane.showInputDialog(this, "Periodo académico activo (ej. 2026-II):",
                "Configurar ciclo académico", JOptionPane.QUESTION_MESSAGE, null, null,
                controladores.sesion.getPeriodoActivo());
        if (entrada != null) {
            controladores.sesion.configurarPeriodo(entrada.toString());
            actualizarEstado();
            Mensajes.info(this, "Periodo activo actualizado a " + controladores.sesion.getPeriodoActivo() + ".");
        }
    }

    private void cerrarSesion() {
        if (Mensajes.confirmar(this, "¿Desea cerrar la sesión actual?")) {
            controladores.sesion.cerrarSesion();
            new LoginFrame(controladores).setVisible(true);
            dispose();
        }
    }

    private void actualizarEstado() {
        var usuario = controladores.sesion.getUsuarioActual();
        String nombre = usuario == null ? "-" : usuario.getNombre() + " (" + usuario.getRol() + ")";
        lblEstado.setText("Usuario: " + nombre + "    |    Periodo académico activo: " + controladores.sesion.getPeriodoActivo());
    }
}
