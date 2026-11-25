package educatec.presentacion.frame;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.enums.EstadoMatricula;
import educatec.negocio.controlador.MatriculaController;
import educatec.negocio.controlador.NotasController;
import educatec.presentacion.modelotabla.MatriculaTM;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.panel.PanelTabla;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class ReportesInternalFrame extends JInternalFrame {
    private static final String TODOS = "Todos";

    private final MatriculaController matriculaController;
    private final NotasController notasController;
    private final JComboBox<Object> cmbEstado = new JComboBox<>();
    private final PanelTabla<MatriculaResumenDTO> tabla = new PanelTabla<>(new MatriculaTM());
    private final JLabel lblResumen = new JLabel(" ");
    private final JTextArea txtNotificaciones = new JTextArea();

    public ReportesInternalFrame(MatriculaController matriculaController, NotasController notasController) {
        super("Reportes", true, true, true, true);
        this.matriculaController = matriculaController;
        this.notasController = notasController;
        construir();
        recargar();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                recargar();
            }
        });
        setSize(980, 560);
    }

    private void construir() {
        cmbEstado.addItem(TODOS);
        for (EstadoMatricula estado : EstadoMatricula.values()) {
            cmbEstado.addItem(estado);
        }
        cmbEstado.setFont(Estilos.FUENTE);
        cmbEstado.addActionListener(e -> Mensajes.ejecutar(this, this::recargar));
        JButton btnActualizar = Estilos.boton("Actualizar", Estilos.SECUNDARIO);
        btnActualizar.addActionListener(e -> Mensajes.ejecutar(this, this::recargar));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filtros.setOpaque(false);
        filtros.add(Estilos.etiqueta("Estado:"));
        filtros.add(cmbEstado);
        filtros.add(btnActualizar);

        lblResumen.setFont(Estilos.NEGRITA);
        lblResumen.setForeground(Estilos.PRIMARIO);

        JPanel pestanaMatriculas = new JPanel(new BorderLayout(0, 6));
        pestanaMatriculas.setBackground(Estilos.FONDO);
        pestanaMatriculas.add(filtros, BorderLayout.NORTH);
        pestanaMatriculas.add(tabla, BorderLayout.CENTER);
        pestanaMatriculas.add(lblResumen, BorderLayout.SOUTH);

        txtNotificaciones.setEditable(false);
        txtNotificaciones.setFont(Estilos.MONO);
        txtNotificaciones.setLineWrap(true);
        txtNotificaciones.setWrapStyleWord(true);
        JPanel pestanaNotificaciones = new JPanel(new BorderLayout());
        pestanaNotificaciones.add(new JScrollPane(txtNotificaciones), BorderLayout.CENTER);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(Estilos.NEGRITA);
        pestanas.addTab("Matrículas y rendimiento", pestanaMatriculas);
        pestanas.addTab("Historial de notificaciones", pestanaNotificaciones);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(pestanas, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void recargar() {
        Object filtro = cmbEstado.getSelectedItem();
        List<MatriculaResumenDTO> datos = matriculaController.listarResumen().stream()
                .filter(m -> filtro == null || TODOS.equals(filtro) || m.estado() == filtro)
                .toList();
        tabla.setDatos(datos);
        double total = datos.stream().mapToDouble(MatriculaResumenDTO::montoTotal).sum();
        long riesgo = datos.stream().filter(m -> m.tienePromedio() && m.promedio() < 11).count();
        lblResumen.setText(String.format("Matrículas: %d  |  Monto total: S/ %.2f  |  Alumnos en riesgo académico: %d",
                datos.size(), total, riesgo));
        txtNotificaciones.setText(String.join("\n\n", notasController.notificaciones()));
    }
}
