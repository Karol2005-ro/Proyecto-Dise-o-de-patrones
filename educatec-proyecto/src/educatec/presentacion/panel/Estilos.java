package educatec.presentacion.panel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public final class Estilos {
    public static final Color PRIMARIO = new Color(0x1F4E79);
    public static final Color SECUNDARIO = new Color(0x2E75B6);
    public static final Color EXITO = new Color(0x2E8B57);
    public static final Color PELIGRO = new Color(0xC0392B);
    public static final Color NEUTRO = new Color(0x5F6B7A);
    public static final Color FONDO = new Color(0xF4F6F9);
    public static final Color FILA_ALTERNA = new Color(0xEAF1F8);
    public static final Font FUENTE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 13);
    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font MONO = new Font("Monospaced", Font.PLAIN, 13);

    private Estilos() {
    }

    public static JButton boton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(NEGRITA);
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(NEGRITA);
        etiqueta.setForeground(PRIMARIO);
        return etiqueta;
    }

    public static JPanel panelTitulado(String titulo) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SECUNDARIO), titulo,
                        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, NEGRITA, PRIMARIO),
                BorderFactory.createEmptyBorder(4, 6, 6, 6)));
        return panel;
    }

    public static void campo(JPanel panel, int fila, int columna, String texto, JComponent componente) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;
        g.gridy = fila;
        g.gridx = columna * 2;
        panel.add(etiqueta(texto), g);
        g.gridx = columna * 2 + 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        componente.setFont(FUENTE);
        panel.add(componente, g);
    }

    public static void tabla(JTable tabla) {
        tabla.setFont(FUENTE);
        tabla.setRowHeight(26);
        tabla.setGridColor(new Color(0xD5DCE6));
        tabla.setSelectionBackground(SECUNDARIO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(NEGRITA);
        tabla.getTableHeader().setBackground(PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 30));
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                                                                    boolean foco, int fila, int columna) {
                java.awt.Component c = super.getTableCellRendererComponent(t, valor, seleccionada, foco, fila, columna);
                if (!seleccionada) {
                    c.setBackground(fila % 2 == 0 ? Color.WHITE : FILA_ALTERNA);
                    c.setForeground(Color.BLACK);
                }
                if (valor instanceof Number) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });
    }
}
