package educatec.presentacion.frame;

import educatec.negocio.controlador.Controladores;
import educatec.presentacion.panel.Estilos;
import educatec.presentacion.panel.Mensajes;
import educatec.presentacion.validacion.ValidadorCamposSwing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

public class LoginFrame extends JFrame {
    private final Controladores controladores;
    private final JTextField txtUsuario = new JTextField(16);
    private final JPasswordField txtClave = new JPasswordField(16);

    public LoginFrame(Controladores controladores) {
        super("EducaTec - Inicio de sesión");
        this.controladores = controladores;
        construir();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 400);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void construir() {
        JLabel titulo = new JLabel("EducaTec", SwingConstants.CENTER);
        titulo.setFont(Estilos.TITULO.deriveFont(30f));
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Gestión académica, matrícula y rendimiento", SwingConstants.CENTER);
        subtitulo.setFont(Estilos.FUENTE);
        subtitulo.setForeground(new Color(0xD6E6F5));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(Estilos.PRIMARIO);
        cabecera.setBorder(BorderFactory.createEmptyBorder(24, 10, 20, 10));
        cabecera.add(titulo, BorderLayout.CENTER);
        cabecera.add(subtitulo, BorderLayout.SOUTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        Estilos.campo(formulario, 0, 0, "Usuario:", txtUsuario);
        Estilos.campo(formulario, 1, 0, "Contraseña:", txtClave);

        JButton btnIngresar = Estilos.boton("Ingresar", Estilos.SECUNDARIO);
        btnIngresar.addActionListener(e -> Mensajes.ejecutar(this, this::ingresar));
        getRootPane().setDefaultButton(btnIngresar);

        JLabel ayuda = new JLabel("Usuarios de prueba: admin / admin123  ·  docente / docente123", SwingConstants.CENTER);
        ayuda.setFont(Estilos.FUENTE.deriveFont(11f));
        ayuda.setForeground(Estilos.NEUTRO);

        JPanel pie = new JPanel(new BorderLayout(0, 10));
        pie.setBackground(Color.WHITE);
        pie.setBorder(BorderFactory.createEmptyBorder(6, 30, 18, 30));
        pie.add(btnIngresar, BorderLayout.NORTH);
        pie.add(ayuda, BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(cabecera, BorderLayout.NORTH);
        raiz.add(formulario, BorderLayout.CENTER);
        raiz.add(pie, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private void ingresar() {
        ValidadorCamposSwing.exigirTexto(txtUsuario, "Usuario");
        controladores.sesion.iniciarSesion(txtUsuario.getText(), txtClave.getPassword());
        PrincipalFrame principal = new PrincipalFrame(controladores);
        principal.setVisible(true);
        dispose();
    }
}
