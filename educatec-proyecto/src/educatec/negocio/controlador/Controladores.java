package educatec.negocio.controlador;

public final class Controladores {
    public final SesionController sesion;
    public final AlumnoController alumno;
    public final CursoController curso;
    public final MatriculaController matricula;
    public final NotasController notas;
    public final PensionController pension;
    public final DocumentosController documentos;
    public final MallaController malla;

    public Controladores(SesionController sesion, AlumnoController alumno, CursoController curso,
                         MatriculaController matricula, NotasController notas, PensionController pension,
                         DocumentosController documentos, MallaController malla) {
        this.sesion = sesion;
        this.alumno = alumno;
        this.curso = curso;
        this.matricula = matricula;
        this.notas = notas;
        this.pension = pension;
        this.documentos = documentos;
        this.malla = malla;
    }
}
