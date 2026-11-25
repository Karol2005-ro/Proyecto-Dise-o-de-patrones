package educatec.presentacion.frame;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.dto.NotaDetalleDTO;
import educatec.modelo.entidad.Curso;
import educatec.modelo.enums.TipoEvaluacion;
import educatec.negocio.controlador.NotasController;
import educatec.presentacion.modelotabla.MatriculaTM;
import educatec.presentacion.modelotabla.NotaTM;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.panel.PanelTabla;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.List;

public class NotasInternalFrame extends JInternalFrame {
    private final NotasController controller;
    private final PanelTabla<MatriculaResumenDTO> tablaMatriculas = new PanelTabla<>(new MatriculaTM());
    private final PanelTabla<NotaDetalleDTO> tablaNotas = new PanelTabla<>(new NotaTM());
    private final JComboBox<Curso> cmbCurso = new JComboBox<>();
    private final JComboBox<TipoEvaluacion> cmbTipo = new JComboBox<>(TipoEvaluacion.values());
    private final JTextField txtNota = new JTextField(6);
    private final JLabel lblPromedio = new JLabel("Promedio: -");
    private final JTextArea txtNotificaciones = new JTextArea();
    private int idMatriculaActual = -1;

    public NotasInternalFrame(NotasController controller) {
        super("Registro de Notas y Notificaciones", true, true, true, true);
        this.controller = controller;
        construir();
        recargarMatriculas();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                recargarMatriculas();
            }
        });
        setSize(1000, 660);
    }

    private void construir() {
        JPanel panelMatriculas = Estilos.panelTitulado("1. Matrículas pagadas (seleccione una)");
        panelMatriculas.setLayout(new BorderLayout());
        panelMatriculas.add(tablaMatriculas, BorderLayout.CENTER);
        tablaMatriculas.addSeleccionListener(this::alSeleccionarMatricula);

        JPanel formulario = Estilos.panelTitulado("2. Registrar calificación");
        formulario.setLayout(new GridBagLayout());
        Estilos.campo(formulario, 0, 0, "Curso:", cmbCurso);
        Estilos.campo(formulario, 0, 1, "Evaluación:", cmbTipo);
        Estilos.campo(formulario, 0, 2, "Nota (0-20):", txtNota);
        JButton btnRegistrar = Estilos.boton("Registrar nota", Estilos.EXITO);
        btnRegistrar.addActionListener(e -> Mensajes.ejecutar(this, this::registrar));
        JPanel accion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        accion.setOpaque(false);
        accion.add(btnRegistrar);
        lblPromedio.setFont(Estilos.NEGRITA);
        lblPromedio.setForeground(Estilos.PRIMARIO);
        accion.add(lblPromedio);
        JPanel panelFormulario = new JPanel(new BorderLayout());
        panelFormulario.setOpaque(false);
        panelFormulario.add(formulario, BorderLayout.CENTER);
        panelFormulario.add(accion, BorderLayout.SOUTH);

        JPanel panelNotas = Estilos.panelTitulado("Notas de la matrícula");
        panelNotas.setLayout(new BorderLayout());
        panelNotas.add(tablaNotas, BorderLayout.CENTER);

        txtNotificaciones.setEditable(false);
        txtNotificaciones.setFont(Estilos.MONO);
        txtNotificaciones.setLineWrap(true);
        txtNotificaciones.setWrapStyleWord(true);
        JPanel panelNotificaciones = Estilos.panelTitulado("Notificaciones a alumnos y apoderados");
        panelNotificaciones.setLayout(new BorderLayout());
        panelNotificaciones.add(new JScrollPane(txtNotificaciones), BorderLayout.CENTER);

        JSplitPane inferior = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelNotas, panelNotificaciones);
        inferior.setResizeWeight(0.5);
        inferior.setBorder(null);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setOpaque(false);
        centro.add(panelFormulario, BorderLayout.NORTH);
        centro.add(inferior, BorderLayout.CENTER);

        JSplitPane principal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelMatriculas, centro);
        principal.setResizeWeight(0.3);
        principal.setBorder(null);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(principal, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void recargarMatriculas() {
        tablaMatriculas.setDatos(controller.listarMatriculasPagadas());
        actualizarNotificaciones();
    }

    private void alSeleccionarMatricula() {
        MatriculaResumenDTO seleccionada = tablaMatriculas.getSeleccionada();
        cmbCurso.removeAllItems();
        if (seleccionada == null) {
            idMatriculaActual = -1;
            tablaNotas.setDatos(List.of());
            lblPromedio.setText("Promedio: -");
            return;
        }
        idMatriculaActual = seleccionada.idMatricula();
        controller.listarCursos(idMatriculaActual).forEach(cmbCurso::addItem);
        recargarNotas();
    }

    private void recargarNotas() {
        if (idMatriculaActual < 0) {
            return;
        }
        List<NotaDetalleDTO> notas = controller.listarNotas(idMatriculaActual);
        tablaNotas.setDatos(notas);
        lblPromedio.setText(notas.isEmpty() ? "Promedio: -" : String.format("Promedio: %.2f", controller.promedio(idMatriculaActual)));
    }

    private void actualizarNotificaciones() {
        txtNotificaciones.setText(String.join("\n\n", controller.notificaciones()));
        txtNotificaciones.setCaretPosition(txtNotificaciones.getDocument().getLength());
    }

    private void registrar() {
        if (idMatriculaActual < 0) {
            Mensajes.error(this, "Seleccione una matrícula pagada.");
            return;
        }
        ValidadorCamposSwing.exigirSeleccion(cmbCurso, "un curso");
        ValidadorCamposSwing.exigirNumero(txtNota, "Nota");
        controller.registrarNota(idMatriculaActual, (Curso) cmbCurso.getSelectedItem(), txtNota.getText(),
                (TipoEvaluacion) cmbTipo.getSelectedItem());
        txtNota.setText("");
        recargarNotas();
        actualizarNotificaciones();
    }
}
