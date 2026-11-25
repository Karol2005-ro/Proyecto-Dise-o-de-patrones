package educatec.datos.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

abstract class BaseMemoriaDAO<T> {
    private final Map<Integer, T> almacen = new LinkedHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger(0);

    protected abstract int obtenerId(T entidad);

    protected abstract void asignarId(T entidad, int id);

    public synchronized Optional<T> buscarPorId(int id) {
        return Optional.ofNullable(almacen.get(id));
    }

    public synchronized List<T> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    public synchronized T insertar(T entidad) {
        int id = secuencia.incrementAndGet();
        asignarId(entidad, id);
        almacen.put(id, entidad);
        return entidad;
    }

    public synchronized void actualizar(T entidad) {
        almacen.put(obtenerId(entidad), entidad);
    }

    public synchronized void eliminar(int id) {
        almacen.remove(id);
    }
}
