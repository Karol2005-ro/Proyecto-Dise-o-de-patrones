package educatec.presentacion.panel;

import educatec.modelo.entidad.Carrera;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelComboCarreras extends JPanel {
    private final JComboBox<Carrera> combo = new JComboBox<>();

    public PanelComboCarreras() {
        super(new BorderLayout());
        setOpaque(false);
        combo.setFont(Estilos.FUENTE);
        add(combo, BorderLayout.CENTER);
    }

    public void setCarreras(List<Carrera> carreras) {
        Carrera actual = getCarreraSeleccionada();
        combo.removeAllItems();
        carreras.forEach(combo::addItem);
        if (actual != null) {
            seleccionar(actual.getId());
        }
    }

    public Carrera getCarreraSeleccionada() {
        return (Carrera) combo.getSelectedItem();
    }

    public void seleccionar(int idCarrera) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == idCarrera) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    public void addActionListener(ActionListener listener) {
        combo.addActionListener(listener);
    }

    public JComboBox<Carrera> getCombo() {
        return combo;
    }
}
