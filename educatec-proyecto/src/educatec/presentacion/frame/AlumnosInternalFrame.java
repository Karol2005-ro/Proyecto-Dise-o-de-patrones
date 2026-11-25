package educatec.presentacion.frame;

import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Carrera;
import educatec.modelo.enums.Escala;
import educatec.modelo.enums.ModalidadAlumno;
import educatec.negocio.controlador.AlumnoController;
import educatec.presentacion.modelotabla.AlumnoTM;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.panel.PanelComboCarreras;
import educatec.presentacion.panel.PanelTabla;
import educatec.presentacion.validacion.FiltroEntradaDocumental;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlumnosInternalFrame extends JInternalFrame {
    private final AlumnoController controller;
    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtNombres = new JTextField(16);
    private final JTextField txtApellidos = new JTextField(16);
    private final JTextField txtEmail = new JTextField(16);
    private final JTextField txtBuscar = new JTextField(18);
    private final JComboBox<ModalidadAlumno> cmbModalidad = new JComboBox<>(ModalidadAlumno.values());
    private final JComboBox<Escala> cmbEscala = new JComboBox<>(Escala.values());
    private final PanelComboCarreras panelCarreras = new PanelComboCarreras();
    private final AlumnoTM modelo = new AlumnoTM();
    private final PanelTabla<Alumno> panelTabla = new PanelTabla<>(modelo);
    private Alumno seleccionado;

    public AlumnosInternalFrame(AlumnoController controller) {
        super("Gestión de Alumnos", true, true, true, true);
        this.controller = controller;
        construir();
        cargarCarreras();
        recargar();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                cargarCarreras();
            }
        });
        setSize(900, 560);
    }

    private void construir() {
        ((AbstractDocument) txtDni.getDocument()).setDocumentFilter(FiltroEntradaDocumental.soloDigitos(8));

        JPanel formulario = Estilos.panelTitulado("Datos del alumno");
        formulario.setLayout(new GridBagLayout());
        Estilos.campo(formulario, 0, 0, "DNI:", txtDni);
        Estilos.campo(formulario, 0, 1, "Nombres:", txtNombres);
        Estilos.campo(formulario, 1, 0, "Apellidos:", txtApellidos);
        Estilos.campo(formulario, 1, 1, "Correo:", txtEmail);
        Estilos.campo(formulario, 2, 0, "Modalidad:", cmbModalidad);
        Estilos.campo(formulario, 2, 1, "Escala de pensión:", cmbEscala);
        Estilos.campo(formulario, 3, 0, "Carrera:", panelCarreras);

        JButton btnRegistrar = Estilos.boton("Registrar", Estilos.EXITO);
        JButton btnActualizar = Estilos.boton("Actualizar", Estilos.SECUNDARIO);
        JButton btnEliminar = Estilos.boton("Eliminar", Estilos.PELIGRO);
        JButton btnLimpiar = Estilos.boton("Limpiar", Estilos.NEUTRO);
        btnRegistrar.addActionListener(e -> Mensajes.ejecutar(this, this::registrar));
        btnActualizar.addActionListener(e -> Mensajes.ejecutar(this, this::actualizar));
        btnEliminar.addActionListener(e -> Mensajes.ejecutar(this, this::eliminar));
        btnLimpiar.addActionListener(e -> limpiar());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        botones.setOpaque(false);
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(botones, BorderLayout.SOUTH);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        busqueda.setOpaque(false);
        busqueda.add(Estilos.etiqueta("Buscar (DNI o nombre):"));
        busqueda.add(txtBuscar);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                recargar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                recargar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                recargar();
            }
        });

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(busqueda, BorderLayout.NORTH);
        centro.add(panelTabla, BorderLayout.CENTER);
        panelTabla.addSeleccionListener(this::cargarSeleccion);

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(norte, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cargarCarreras() {
        List<Carrera> carreras = controller.listarCarreras();
        panelCarreras.setCarreras(carreras);
        Map<Integer, String> mapa = new HashMap<>();
        carreras.forEach(c -> mapa.put(c.getId(), c.getNombre()));
        modelo.setCarreras(mapa);
    }

    private void recargar() {
        panelTabla.setDatos(controller.listar(txtBuscar.getText()));
    }

    private void cargarSeleccion() {
        Alumno alumno = panelTabla.getSeleccionada();
        if (alumno == null) {
            return;
        }
        seleccionado = alumno;
        txtDni.setText(alumno.getDni());
        txtNombres.setText(alumno.getNombres());
        txtApellidos.setText(alumno.getApellidos());
        txtEmail.setText(alumno.getEmail() == null ? "" : alumno.getEmail());
        cmbModalidad.setSelectedItem(alumno.getModalidad());
        cmbEscala.setSelectedItem(alumno.getEscalaPension());
        panelCarreras.seleccionar(alumno.getIdCarrera());
    }

    private void validarFormulario() {
        ValidadorCamposSwing.exigirDni(txtDni);
        ValidadorCamposSwing.exigirTexto(txtNombres, "Nombres");
        ValidadorCamposSwing.exigirTexto(txtApellidos, "Apellidos");
        ValidadorCamposSwing.exigirCorreoOpcional(txtEmail);
        ValidadorCamposSwing.exigirSeleccion(panelCarreras.getCombo(), "una carrera");
    }

    private void registrar() {
        validarFormulario();
        controller.registrar(txtDni.getText(), txtNombres.getText(), txtApellidos.getText(), txtEmail.getText(),
                (ModalidadAlumno) cmbModalidad.getSelectedItem(), (Escala) cmbEscala.getSelectedItem(),
                panelCarreras.getCarreraSeleccionada());
        recargar();
        limpiar();
        Mensajes.info(this, "Alumno registrado correctamente.");
    }

    private void actualizar() {
        if (seleccionado == null) {
            Mensajes.error(this, "Seleccione un alumno de la tabla para actualizar.");
            return;
        }
        validarFormulario();
        controller.actualizar(seleccionado.getId(), txtDni.getText(), txtNombres.getText(), txtApellidos.getText(),
                txtEmail.getText(), (ModalidadAlumno) cmbModalidad.getSelectedItem(),
                (Escala) cmbEscala.getSelectedItem(), panelCarreras.getCarreraSeleccionada());
        recargar();
        limpiar();
        Mensajes.info(this, "Alumno actualizado correctamente.");
    }

    private void eliminar() {
        if (seleccionado == null) {
            Mensajes.error(this, "Seleccione un alumno de la tabla para eliminar.");
            return;
        }
        if (Mensajes.confirmar(this, "¿Desea eliminar al alumno " + seleccionado.getNombreCompleto() + "?")) {
            controller.eliminar(seleccionado.getId());
            recargar();
            limpiar();
        }
    }

    private void limpiar() {
        seleccionado = null;
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        cmbModalidad.setSelectedIndex(0);
        cmbEscala.setSelectedIndex(0);
        panelTabla.limpiarSeleccion();
        txtDni.requestFocusInWindow();
    }
}
