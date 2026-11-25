package educatec.presentacion.frame;

import educatec.modelo.entidad.Carrera;
import educatec.modelo.entidad.Curso;
import educatec.negocio.controlador.CursoController;
import educatec.presentacion.modelotabla.CursoTM;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.panel.PanelComboCarreras;
import educatec.presentacion.panel.PanelTabla;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursosInternalFrame extends JInternalFrame {
    private final CursoController controller;
    private final JTextField txtCodigo = new JTextField(10);
    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtPrerrequisito = new JTextField(10);
    private final JTextField txtBuscar = new JTextField(18);
    private final JSpinner spnCreditos = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
    private final JSpinner spnHoras = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
    private final PanelComboCarreras panelCarreras = new PanelComboCarreras();
    private final CursoTM modelo = new CursoTM();
    private final PanelTabla<Curso> panelTabla = new PanelTabla<>(modelo);
    private Curso seleccionado;

    public CursosInternalFrame(CursoController controller) {
        super("Gestión de Cursos", true, true, true, true);
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
        JPanel formulario = Estilos.panelTitulado("Datos del curso");
        formulario.setLayout(new GridBagLayout());
        Estilos.campo(formulario, 0, 0, "Código:", txtCodigo);
        Estilos.campo(formulario, 0, 1, "Nombre:", txtNombre);
        Estilos.campo(formulario, 1, 0, "Créditos:", spnCreditos);
        Estilos.campo(formulario, 1, 1, "Horas lectivas:", spnHoras);
        Estilos.campo(formulario, 2, 0, "Prerrequisito (código):", txtPrerrequisito);
        Estilos.campo(formulario, 2, 1, "Carrera:", panelCarreras);

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
        busqueda.add(Estilos.etiqueta("Buscar (código o nombre):"));
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
        Curso curso = panelTabla.getSeleccionada();
        if (curso == null) {
            return;
        }
        seleccionado = curso;
        txtCodigo.setText(curso.getCodigo());
        txtNombre.setText(curso.getNombre());
        spnCreditos.setValue(curso.getCreditos());
        spnHoras.setValue(curso.getHorasLectivas());
        txtPrerrequisito.setText(curso.tienePrerrequisito() ? curso.getCodigoPrerrequisito() : "");
        panelCarreras.seleccionar(curso.getIdCarrera());
    }

    private void validarFormulario() {
        ValidadorCamposSwing.exigirTexto(txtCodigo, "Código");
        ValidadorCamposSwing.exigirTexto(txtNombre, "Nombre");
        ValidadorCamposSwing.exigirSeleccion(panelCarreras.getCombo(), "una carrera");
    }

    private void registrar() {
        validarFormulario();
        controller.registrar(txtCodigo.getText(), txtNombre.getText(), (Integer) spnCreditos.getValue(),
                (Integer) spnHoras.getValue(), panelCarreras.getCarreraSeleccionada(), txtPrerrequisito.getText());
        recargar();
        limpiar();
        Mensajes.info(this, "Curso registrado correctamente.");
    }

    private void actualizar() {
        if (seleccionado == null) {
            Mensajes.error(this, "Seleccione un curso de la tabla para actualizar.");
            return;
        }
        validarFormulario();
        controller.actualizar(seleccionado.getId(), txtCodigo.getText(), txtNombre.getText(),
                (Integer) spnCreditos.getValue(), (Integer) spnHoras.getValue(),
                panelCarreras.getCarreraSeleccionada(), txtPrerrequisito.getText());
        recargar();
        limpiar();
        Mensajes.info(this, "Curso actualizado correctamente.");
    }

    private void eliminar() {
        if (seleccionado == null) {
            Mensajes.error(this, "Seleccione un curso de la tabla para eliminar.");
            return;
        }
        if (Mensajes.confirmar(this, "¿Desea eliminar el curso " + seleccionado.getCodigo() + "?")) {
            controller.eliminar(seleccionado.getId());
            recargar();
            limpiar();
        }
    }

    private void limpiar() {
        seleccionado = null;
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrerrequisito.setText("");
        spnCreditos.setValue(3);
        spnHoras.setValue(4);
        panelTabla.limpiarSeleccion();
        txtCodigo.requestFocusInWindow();
    }
}
