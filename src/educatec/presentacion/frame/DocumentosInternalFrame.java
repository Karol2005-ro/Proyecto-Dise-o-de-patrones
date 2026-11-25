
package educatec.presentacion.frame;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.enums.TipoDocumento;
import educatec.negocio.controlador.DocumentosController;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.validacion.ValidadorCamposSwing;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

public class DocumentosInternalFrame extends JInternalFrame {

    private final DocumentosController controller;
    private final JComboBox<Alumno> cmbAlumno = new JComboBox<>();
    private final JComboBox<TipoDocumento> cmbTipo =
            new JComboBox<>(TipoDocumento.values());
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

        setSize(850, 620);
        setMinimumSize(new java.awt.Dimension(800, 580));
    }

    private void construir() {

        // Panel de datos
        JPanel filtros = Estilos.panelTitulado("Datos de emisión");
        filtros.setLayout(new GridBagLayout());

        cmbAlumno.setFont(Estilos.FUENTE);
        cmbAlumno.setPreferredSize(new java.awt.Dimension(300, 28));

        cmbTipo.setFont(Estilos.FUENTE);

        JButton btnEmitir = Estilos.boton(
                "Emitir documento", Estilos.SECUNDARIO);

        JButton btnGuardar = Estilos.boton(
                "Guardar como TXT", Estilos.EXITO);

        btnEmitir.addActionListener(
                e -> Mensajes.ejecutar(this, this::emitir));

        btnGuardar.addActionListener(
                e -> Mensajes.ejecutar(this, this::guardar));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filtros.add(Estilos.etiqueta("Alumno:"), gbc);

        gbc.gridx = 1;
        filtros.add(cmbAlumno, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filtros.add(Estilos.etiqueta("Documento:"), gbc);

        gbc.gridx = 1;
        filtros.add(cmbTipo, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        filtros.add(btnEmitir, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        filtros.add(btnGuardar, gbc);

        // Panel de vista previa
        txtVista.setEditable(false);
        txtVista.setFont(Estilos.MONO);
        txtVista.setMargin(new Insets(10, 12, 10, 12));

        JPanel vista = Estilos.panelTitulado("Vista previa");
        vista.setLayout(new BorderLayout());
        vista.add(new JScrollPane(txtVista), BorderLayout.CENTER);

        // Panel principal
        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        txtVista.setText(
                controller.emitir(
                        (Alumno) cmbAlumno.getSelectedItem(),
                        (TipoDocumento) cmbTipo.getSelectedItem()
                )
        );

        txtVista.setCaretPosition(0);
    }

    private void guardar() {
        if (txtVista.getText().isBlank()) {
            Mensajes.error(this, "Emita primero un documento.");
            return;
        }

        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(
                new File("documento_educatec.txt"));

        if (selector.showSaveDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            try {
                Files.writeString(
                        selector.getSelectedFile().toPath(),
                        txtVista.getText(),
                        StandardCharsets.UTF_8
                );

                Mensajes.info(
                        this, "Documento guardado correctamente.");

            } catch (IOException ex) {
                Mensajes.error(
                        this,
                        "No se pudo guardar el archivo: "
                                + ex.getMessage());
            }
        }
    }
}
