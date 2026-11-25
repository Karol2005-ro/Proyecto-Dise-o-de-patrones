package educatec.modelo.entidad;

import java.time.LocalDate;

public class Pago {
    public static final String APROBADO = "APROBADO";
    public static final String RECHAZADO = "RECHAZADO";

    private int id;
    private int idMatricula;
    private double monto;
    private LocalDate fechaPago;
    private String codigoOperacionBanco;
    private String estadoPago;

    public Pago() {
    }

    public Pago(int id, int idMatricula, double monto, LocalDate fechaPago,
                String codigoOperacionBanco, String estadoPago) {
        this.id = id;
        this.idMatricula = idMatricula;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.codigoOperacionBanco = codigoOperacionBanco;
        this.estadoPago = estadoPago;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public String getCodigoOperacionBanco() { return codigoOperacionBanco; }
    public void setCodigoOperacionBanco(String codigoOperacionBanco) { this.codigoOperacionBanco = codigoOperacionBanco; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public boolean estaAprobado() {
        return APROBADO.equals(estadoPago);
    }
}
