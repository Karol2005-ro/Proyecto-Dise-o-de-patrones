package educatec.datos.dao;

public interface IEscrituraDAO<T> {
    T insertar(T entidad);

    void actualizar(T entidad);

    void eliminar(int id);
}
