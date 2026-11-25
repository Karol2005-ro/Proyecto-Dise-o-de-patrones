package educatec.presentacion.modelotabla;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class ModeloTablaGenerico<T> extends AbstractTableModel {
    private final String[] columnas;
    private List<T> filas = new ArrayList<>();

    protected ModeloTablaGenerico(String... columnas) {
        this.columnas = columnas;
    }

    protected abstract Object valor(T fila, int columna);

    public void setFilas(List<T> nuevas) {
        this.filas = new ArrayList<>(nuevas);
        fireTableDataChanged();
    }

    public T getFila(int indice) {
        return indice >= 0 && indice < filas.size() ? filas.get(indice) : null;
    }

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int columna) {
        return columnas[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        return valor(filas.get(fila), columna);
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
