package educatec.presentacion.frame;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.TipoDocumento;
import educatec.negocio.controlador.DocumentosController;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class DocumentosInternalFrame extends JInternalFrame {
    private final DocumentosController controller;
    private final JComboBox<Alumno> cmbAlumno = new JComboBox<>();
    private final JComboBox<TipoDocumento> cmbTipo = new JComboBox<>(TipoDocumento.values());
    private final JTextArea txtVista = new JTextArea();

    public DocumentosInternalFrame(DocumentosController controller) {
        super("Emisión de Documentos y Carnets", true, true, true, true);
        this.controller = controller;
        construir();
        cargarAlumnos();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                cargarAlumnos();
            }
        });
        setSize(760, 560);
    }

    private void construir() {
        JPanel filtros = Estilos.panelTitulado("Datos de emisión");
        filtros.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        cmbAlumno.setFont(Estilos.FUENTE);
        cmbAlumno.setPreferredSize(new java.awt.Dimension(320, 28));
        cmbTipo.setFont(Estilos.FUENTE);
        JButton btnEmitir = Estilos.boton("Emitir documento", Estilos.SECUNDARIO);
        JButton btnGuardar = Estilos.boton("Guardar como TXT", Estilos.EXITO);
        btnEmitir.addActionListener(e -> Mensajes.ejecutar(this, this::emitir));
        btnGuardar.addActionListener(e -> Mensajes.ejecutar(this, this::guardar));
        filtros.add(Estilos.etiqueta("Alumno:"));
        filtros.add(cmbAlumno);
        filtros.add(Estilos.etiqueta("Documento:"));
        filtros.add(cmbTipo);
        filtros.add(btnEmitir);
        filtros.add(btnGuardar);

        txtVista.setEditable(false);
        txtVista.setFont(Estilos.MONO);
        txtVista.setMargin(new java.awt.Insets(10, 12, 10, 12));
        JPanel vista = Estilos.panelTitulado("Vista previa");
        vista.setLayout(new BorderLayout());
        vista.add(new JScrollPane(txtVista), BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(filtros, BorderLayout.NORTH);
        raiz.add(vista, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cargarAlumnos() {
        Alumno actual = (Alumno) cmbAlumno.getSelectedItem();
        cmbAlumno.removeAllItems();
        controller.listarAlumnos().forEach(a -> {
            cmbAlumno.addItem(a);
            if (actual != null && a.getId() == actual.getId()) {
                cmbAlumno.setSelectedItem(a);
            }
        });
    }

    private void emitir() {
        ValidadorCamposSwing.exigirSeleccion(cmbAlumno, "un alumno");
        txtVista.setText(controller.emitir((Alumno) cmbAlumno.getSelectedItem(), (TipoDocumento) cmbTipo.getSelectedItem()));
        txtVista.setCaretPosition(0);
    }

    private void guardar() {
        if (txtVista.getText().isBlank()) {
            Mensajes.error(this, "Emita primero un documento.");
            return;
        }
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("documento_educatec.txt"));
        if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(selector.getSelectedFile().toPath(), txtVista.getText(), StandardCharsets.UTF_8);
                Mensajes.info(this, "Documento guardado correctamente.");
            } catch (IOException ex) {
                Mensajes.error(this, "No se pudo guardar el archivo: " + ex.getMessage());
            }
        }
    }
}
