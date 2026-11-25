package educatec.negocio.servicio.impl;

import educatec.datos.dao.ICarreraDAO;
import educatec.datos.dao.ICursoDAO;
import educatec.negocio.patrones.composite.Carrera;
import educatec.negocio.patrones.composite.ComponenteAcademico;
import educatec.negocio.patrones.composite.Curso;
import educatec.negocio.patrones.composite.Facultad;
import educatec.negocio.patrones.composite.PlanEstudios;
import educatec.negocio.servicio.ServicioMalla;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioMallaImpl implements ServicioMalla {
    private final ICarreraDAO carreraDAO;
    private final ICursoDAO cursoDAO;

    public ServicioMallaImpl(ICarreraDAO carreraDAO, ICursoDAO cursoDAO) {
        this.carreraDAO = carreraDAO;
        this.cursoDAO = cursoDAO;
    }

    @Override
    public List<ComponenteAcademico> construirMalla() {
        Map<String, Facultad> facultades = new LinkedHashMap<>();
        for (educatec.modelo.entidad.Carrera entidad : carreraDAO.listarTodos()) {
            Facultad facultad = facultades.computeIfAbsent(entidad.getFacultad(), Facultad::new);
            Carrera carrera = new Carrera(entidad.getNombre());
            PlanEstudios plan = new PlanEstudios("Plan de estudios " + entidad.getCodigo());
            for (educatec.modelo.entidad.Curso curso : cursoDAO.listarPorCarrera(entidad.getId())) {
                plan.agregar(new Curso(curso.getCodigo() + " - " + curso.getNombre(),
                        curso.getCreditos(), curso.getHorasLectivas()));
            }
            carrera.agregar(plan);
            facultad.agregar(carrera);
        }
        return new ArrayList<>(facultades.values());
    }
}
