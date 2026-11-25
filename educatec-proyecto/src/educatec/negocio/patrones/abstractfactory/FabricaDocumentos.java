package educatec.negocio.patrones.abstractfactory;

import educatec.modelo.enums.ModalidadAlumno;

public interface FabricaDocumentos {
    ModalidadAlumno modalidad();

    Carnet crearCarnet();

    Constancia crearConstancia();

    FichaMatricula crearFichaMatricula();
}
