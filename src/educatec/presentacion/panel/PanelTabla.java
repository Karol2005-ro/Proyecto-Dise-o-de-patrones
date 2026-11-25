package educatec.presentacion.panel;

import educatec.presentacion.modelotabla.ModeloTablaGenerico;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class PanelTabla<T> extends JPanel {
    private final ModeloTablaGenerico<T> modelo;
    private final JTable tabla;

    public PanelTabla(ModeloTablaGenerico<T> modelo) {
        super(new BorderLayout());
        this.modelo = modelo;
        this.tabla = new JTable(modelo);
        Estilos.tabla(tabla);
        tabla.setAutoCreateRowSorter(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void setDatos(List<T> datos) {
        modelo.setFilas(datos);
    }

    public T getSeleccionada() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : modelo.getFila(tabla.convertRowIndexToModel(fila));
    }

    public List<T> getSeleccionadas() {
        List<T> seleccionadas = new ArrayList<>();
        for (int fila : tabla.getSelectedRows()) {
            seleccionadas.add(modelo.getFila(tabla.convertRowIndexToModel(fila)));
        }
        return seleccionadas;
    }

    public void permitirSeleccionMultiple() {
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void addSeleccionListener(Runnable accion) {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                accion.run();
            }
        });
    }

    public void limpiarSeleccion() {
        tabla.clearSelection();
    }

    public JTable getTabla() {
        return tabla;
    }
}
