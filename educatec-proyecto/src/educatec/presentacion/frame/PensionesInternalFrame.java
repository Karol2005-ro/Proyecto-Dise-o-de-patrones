package educatec.presentacion.frame;

import educatec.modelo.dto.ReportePensionDTO;
import educatec.modelo.enums.Escala;
import educatec.negocio.controlador.PensionController;
import educatec.presentacion.modelotabla.PensionTM;
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
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class PensionesInternalFrame extends JInternalFrame {
    private final PensionController controller;
    private final JTextField txtMontoBase = new JTextField("1200.00", 10);
    private final JComboBox<Escala> cmbEscala = new JComboBox<>(Escala.values());
    private final JLabel lblResumen = new JLabel(" ");
    private final PanelTabla<ReportePensionDTO> tabla = new PanelTabla<>(new PensionTM());

    public PensionesInternalFrame(PensionController controller) {
        super("Categorización de Pensiones", true, true, true, true);
        this.controller = controller;
        construir();
        Mensajes.ejecutar(this, this::calcular);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                Mensajes.ejecutar(PensionesInternalFrame.this, PensionesInternalFrame.this::calcular);
            }
        });
        setSize(900, 540);
    }

    private void construir() {
        JPanel calculo = Estilos.panelTitulado("Cálculo de pensión");
        calculo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        calculo.add(Estilos.etiqueta("Monto base (S/):"));
        txtMontoBase.setFont(Estilos.FUENTE);
        calculo.add(txtMontoBase);
        JButton btnCalcular = Estilos.boton("Calcular pensiones", Estilos.SECUNDARIO);
        btnCalcular.addActionListener(e -> Mensajes.ejecutar(this, this::calcular));
        calculo.add(btnCalcular);

        JPanel asignacion = Estilos.panelTitulado("Asignar escala al alumno seleccionado");
        asignacion.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        cmbEscala.setFont(Estilos.FUENTE);
        asignacion.add(cmbEscala);
        JButton btnAsignar = Estilos.boton("Asignar escala", Estilos.EXITO);
        btnAsignar.addActionListener(e -> Mensajes.ejecutar(this, this::asignar));
        asignacion.add(btnAsignar);

        JPanel norte = new JPanel(new BorderLayout(8, 0));
        norte.setOpaque(false);
        norte.add(calculo, BorderLayout.WEST);
        norte.add(asignacion, BorderLayout.CENTER);

        lblResumen.setFont(Estilos.NEGRITA);
        lblResumen.setForeground(Estilos.PRIMARIO);
        lblResumen.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(norte, BorderLayout.NORTH);
        raiz.add(tabla, BorderLayout.CENTER);
        raiz.add(lblResumen, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void calcular() {
        ValidadorCamposSwing.exigirNumero(txtMontoBase, "Monto base");
        List<ReportePensionDTO> reporte = controller.generarReporte(txtMontoBase.getText());
        tabla.setDatos(reporte);
        double totalDescuento = reporte.stream().mapToDouble(ReportePensionDTO::descuento).sum();
        double totalFinal = reporte.stream().mapToDouble(ReportePensionDTO::montoFinal).sum();
        lblResumen.setText(String.format("Alumnos: %d  |  Descuento total: S/ %.2f  |  Recaudación estimada: S/ %.2f",
                reporte.size(), totalDescuento, totalFinal));
    }

    private void asignar() {
        ReportePensionDTO seleccionado = tabla.getSeleccionada();
        if (seleccionado == null) {
            Mensajes.error(this, "Seleccione un alumno de la tabla.");
            return;
        }
        controller.cambiarEscala(seleccionado.idAlumno(), (Escala) cmbEscala.getSelectedItem());
        calcular();
        Mensajes.info(this, "Escala actualizada correctamente.");
    }
}
