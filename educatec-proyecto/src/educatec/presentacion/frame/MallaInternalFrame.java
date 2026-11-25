package educatec.presentacion.frame;

import educatec.negocio.controlador.MallaController;
import educatec.negocio.patrones.composite.ComponenteAcademico;
import educatec.presentacion.panel.Estilos;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class MallaInternalFrame extends JInternalFrame {
    private final MallaController controller;
    private final JTree arbol = new JTree();
    private final JLabel lblTotales = new JLabel("Seleccione un nodo para ver sus totales.");

    public MallaInternalFrame(MallaController controller) {
        super("Estructura Curricular y Malla Académica", true, true, true, true);
        this.controller = controller;
        construir();
        cargar();
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                cargar();
            }
        });
        setSize(760, 560);
    }

    private void construir() {
        arbol.setFont(Estilos.FUENTE);
        arbol.setRowHeight(24);
        arbol.addTreeSelectionListener(e -> {
            Object nodo = arbol.getLastSelectedPathComponent();
            if (nodo instanceof DefaultMutableTreeNode n && n.getUserObject() instanceof ComponenteAcademico c) {
                lblTotales.setText(String.format("%s  →  %d créditos totales y %d horas lectivas totales",
                        c.getNombre(), c.getCreditos(), c.getHorasLectivas()));
            }
        });
        lblTotales.setFont(Estilos.NEGRITA);
        lblTotales.setForeground(Estilos.PRIMARIO);

        JButton btnActualizar = Estilos.boton("Actualizar malla", Estilos.SECUNDARIO);
        btnActualizar.addActionListener(e -> cargar());
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        barra.setOpaque(false);
        barra.add(btnActualizar);

        JPanel contenido = Estilos.panelTitulado("Facultad → Carrera → Plan de estudios → Cursos");
        contenido.setLayout(new BorderLayout());
        contenido.add(new JScrollPane(arbol), BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilos.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(barra, BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);
        raiz.add(lblTotales, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void cargar() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Instituto Superior EducaTec");
        for (ComponenteAcademico facultad : controller.obtenerMalla()) {
            raiz.add(construirNodo(facultad));
        }
        arbol.setModel(new DefaultTreeModel(raiz));
        for (int i = 0; i < arbol.getRowCount(); i++) {
            arbol.expandRow(i);
        }
    }

    private DefaultMutableTreeNode construirNodo(ComponenteAcademico componente) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(componente);
        for (ComponenteAcademico hijo : componente.getHijos()) {
            nodo.add(construirNodo(hijo));
        }
        return nodo;
    }
}
