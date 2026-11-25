package educatec.presentacion.validacion;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FiltroEntradaDocumental extends DocumentFilter {
    private final int longitudMaxima;
    private final String patronPermitido;

    public FiltroEntradaDocumental(int longitudMaxima, String patronPermitido) {
        this.longitudMaxima = longitudMaxima;
        this.patronPermitido = patronPermitido;
    }

    public static FiltroEntradaDocumental soloDigitos(int longitudMaxima) {
        return new FiltroEntradaDocumental(longitudMaxima, "\\d*");
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String texto, AttributeSet atributos) throws BadLocationException {
        replace(fb, offset, 0, texto, atributos);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int longitud, String texto, AttributeSet atributos) throws BadLocationException {
        if (texto == null) {
            super.replace(fb, offset, longitud, texto, atributos);
            return;
        }
        int resultante = fb.getDocument().getLength() - longitud + texto.length();
        if (resultante <= longitudMaxima && texto.matches(patronPermitido)) {
            super.replace(fb, offset, longitud, texto, atributos);
        }
    }
}
