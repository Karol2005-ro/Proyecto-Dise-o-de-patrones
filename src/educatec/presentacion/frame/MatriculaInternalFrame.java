package educatec.presentacion.frame;

import educatec.modelo.dto.MatriculaResumenDTO;
import educatec.modelo.entidad.Alumno;
import educatec.modelo.entidad.Curso;
import educatec.modelo.entidad.Pago;
import educatec.modelo.enums.EstadoMatricula;
import educatec.modelo.enums.TipoServicio;
import educatec.negocio.controlador.MatriculaController;
import educatec.negocio.controlador.SesionController;
import educatec.presentacion.modelotabla.CursoTM;
import educatec.presentacion.modelotabla.MatriculaTM;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.panel.PanelTabla;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MatriculaInternalFrame extends JInternalFrame {
    private final MatriculaController controller;
    private final SesionController sesion;
    private final JComboBox<Alumno> cmbAlumno = new JComboBox<>();
    private final JLabel lblAlumno = new JLabel(" ");
    private final JLabel lblPeriodo = new JLabel();
    private final Map<TipoServicio, JCheckBox> servicios = new LinkedHashMap<>();
    private final PanelTabla<Curso> tablaCursos = new PanelTabla<>(new CursoTM());
    private final PanelTabla<MatriculaResumenDTO> tablaMatriculas = new PanelTabla<>(new MatriculaTM());

    public MatriculaInternalFrame(MatriculaController controller, SesionController sesion) {
        super("Matrícula de Alumnos", true, true, true, true);
        this.controller = controller;
        this.sesion = sesion;
        construir();
        cargarAlumnos();
        recargarMatriculas();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                cargarAlumnos();
                recargarMatriculas();
            }
        });
        setSize(1000, 640);
    }

    private void construir() {
        lblAlumno.setFont(Estilos.FUENTE);
        lblPeriodo.setFont(Estilos.NEGRITA);
        lblPeriodo.setForeground(Estilos.EXITO);

        JPanel seleccion = Estilos.panelTitulado("1. Alumno y periodo");
        seleccion.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        seleccion.add(Estilos.etiqueta("Alumno:"));
        cmbAlumno.setFont(Estilos.FUENTE);
        cmbAlumno.setPreferredSize(new java.awt.Dimension(320, 28));
        cmbAlumno.addActionListener(e -> alSeleccionarAlumno());
        seleccion.add(cmbAlumno);
        seleccion.add(lblAlumno);
        seleccion.add(lblPeriodo);

        tablaCursos.permitirSeleccionMultiple();
        JPanel panelCursos = Estilos.panelTitulado("2. Cursos de la carrera (seleccione uno o varios)");
        panelCursos.setLayout(new BorderLayout(0, 6));
        panelCursos.add(tablaCursos, BorderLayout.CENTER);

        JPanel panelServicios = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        panelServicios.setOpaque(false);
        panelServicios.add(Estilos.etiqueta("3. Servicios adicionales:"));
        for (TipoServicio tipo : TipoServicio.values()) {
            JCheckBox check = new JCheckBox(tipo.toString());
            check.setFont(Estilos.FUENTE);
            check.setOpaque(false);
            servicios.put(tipo, check);
            panelServicios.add(check);
        }
        panelCursos.add(panelServicios, BorderLayout.SOUTH);

        JButton btnMatricular = Estilos.boton("Matricular", Estilos.EXITO);
        JButton btnPagar = Estilos.boton("Pagar matrícula", Estilos.SECUNDARIO);
        JButton btnAnular = Estilos.boton("Anular", Estilos.PELIGRO);
        btnMatricular.addActionListener(e -> Mensajes.ejecutar(this, this::matricular));
        btnPagar.addActionListener(e -> Mensajes.ejecutar(this, this::pagar));
        btnAnular.addActionListener(e -> Mensajes.ejecutar(this, this::anular));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        botones.setOpaque(false);
        botones.add(btnMatricular);
        botones.add(btnPagar);
        botones.add(btnAnular);

        JPanel panelMatriculas = Estilos.panelTitulado("Matrículas registradas");
        panelMatriculas.setLayout(new BorderLayout());
        panelMatriculas.add(tablaMatriculas, BorderLayout.CENTER);
        panelMatriculas.add(botones, BorderLayout.NORTH);

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelCursos, panelMatriculas);
        division.setResizeWeight(0.5);
        division.setBorder(null);
        division.setOpaque(false);

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(seleccion, BorderLayout.NORTH);
        raiz.add(division, BorderLayout.CENTER);
        setContentPane(raiz);
    }

    private void cargarAlumnos() {
        Alumno actual = (Alumno) cmbAlumno.getSelectedItem();
        cmbAlumno.removeAllItems();
        List<Alumno> alumnos = controller.listarAlumnos();
        alumnos.forEach(cmbAlumno::addItem);
        if (actual != null) {
            for (Alumno a : alumnos) {
                if (a.getId() == actual.getId()) {
                    cmbAlumno.setSelectedItem(a);
                }
            }
        }
        lblPeriodo.setText("Periodo activo: " + sesion.getPeriodoActivo());
        alSeleccionarAlumno();
    }

    private void alSeleccionarAlumno() {
        Alumno alumno = (Alumno) cmbAlumno.getSelectedItem();
        if (alumno == null) {
            lblAlumno.setText(" ");
            tablaCursos.setDatos(List.of());
            return;
        }
        lblAlumno.setText("| " + alumno.getModalidad() + " | " + alumno.getEscalaPension());
        tablaCursos.setDatos(controller.listarCursosDeCarrera(alumno.getIdCarrera()));
    }

    private void recargarMatriculas() {
        tablaMatriculas.setDatos(controller.listarResumen());
    }

    private void matricular() {
        ValidadorCamposSwing.exigirSeleccion(cmbAlumno, "un alumno");
        List<Curso> cursos = tablaCursos.getSeleccionadas();
        if (cursos.isEmpty()) {
            Mensajes.error(this, "Seleccione al menos un curso de la tabla.");
            return;
        }
        List<TipoServicio> elegidos = new ArrayList<>();
        servicios.forEach((tipo, check) -> {
            if (check.isSelected()) {
                elegidos.add(tipo);
            }
        });
        MatriculaResumenDTO resultado = controller.matricular((Alumno) cmbAlumno.getSelectedItem(), cursos, elegidos);
        servicios.values().forEach(c -> c.setSelected(false));
        recargarMatriculas();
        Mensajes.info(this, String.format("Matrícula N° %d registrada.%nCursos: %d | Créditos: %d%nMonto a pagar: S/ %.2f",
                resultado.idMatricula(), resultado.totalCursos(), resultado.totalCreditos(), resultado.montoTotal()));
    }

    private void pagar() {
        MatriculaResumenDTO seleccionada = tablaMatriculas.getSeleccionada();
        if (seleccionada == null) {
            Mensajes.error(this, "Seleccione una matrícula de la tabla.");
            return;
        }
        if (seleccionada.estado() != EstadoMatricula.PENDIENTE) {
            Mensajes.error(this, "Solo se pueden pagar matrículas pendientes.");
            return;
        }
        String cuenta = JOptionPane.showInputDialog(this,
                String.format("Monto a pagar: S/ %.2f%nIngrese el número de cuenta (10 a 20 dígitos):", seleccionada.montoTotal()),
                "Pago bancario", JOptionPane.QUESTION_MESSAGE);
        if (cuenta == null) {
            return;
        }
        Pago pago = controller.pagar(seleccionada.idMatricula(), cuenta);
        recargarMatriculas();
        Mensajes.info(this, "Pago aprobado.\nCódigo de operación: " + pago.getCodigoOperacionBanco());
    }

    private void anular() {
        MatriculaResumenDTO seleccionada = tablaMatriculas.getSeleccionada();
        if (seleccionada == null) {
            Mensajes.error(this, "Seleccione una matrícula de la tabla.");
            return;
        }
        if (Mensajes.confirmar(this, "¿Desea anular la matrícula N° " + seleccionada.idMatricula() + "?")) {
            controller.anular(seleccionada.idMatricula());
            recargarMatriculas();
        }
    }
}
