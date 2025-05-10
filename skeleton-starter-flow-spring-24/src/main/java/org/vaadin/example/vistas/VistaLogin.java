package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Usuario;

@Route("inicio-sesion")
@PageTitle("Iniciar Sesion | RESET")
@CssImport("./styles/styles.css")
public class VistaLogin extends VerticalLayout {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public VistaLogin(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
        addClassName("vista-paneles");
        setSizeFull();

        // Títulos
        H1 titulo = new H1("RESET - Iniciar Sesión");
        H2 subtitulo = new H2("Accede a tu cuenta y continua tu proceso.");
        subtitulo.getStyle().set("font-size", "1.2rem").set("font-weight", "500");

        // Campos
        TextField emailField = new TextField("Email");
        emailField.setWidth("400px");

        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidth("400px");

        // Botones
        Button loginButton = new Button("Iniciar sesión", e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                Notification.show("Completa todos los campos.", 3000, Notification.Position.MIDDLE);
                return;
            }
            realizarLogin(email, password);
        });
        loginButton.addClassName("boton-principal");

        Button volverButton = new Button("Volver a Inicio", e -> getUI().ifPresent(ui -> ui.navigate("")));
        volverButton.addClassName("boton-principal");

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton, loginButton);
        botonesLayout.setSpacing(true);
        botonesLayout.setWidth("400px");
        botonesLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Contenedor visual
        VerticalLayout layout = new VerticalLayout(titulo, subtitulo, emailField, passwordField, botonesLayout);
        layout.addClassName("seccion");
        layout.setAlignItems(Alignment.CENTER);

        add(layout);
        setAlignItems(Alignment.CENTER);
    }

    private void realizarLogin(String email, String password) {
        Usuario usuario = servicioUsuario.loginYObtenerUsuario(email, password);

        if (usuario == null) {
            Notification.show("Correo o contraseña incorrectos", 3000, Notification.Position.MIDDLE);
            return;
        }

        UsuarioSesion.setUsuario(usuario);

        if (usuario.getTipoUsuario() == Usuario.TipoUsuario.LUDOPATA) {
            UI.getCurrent().navigate("dashboard-ludopata");
        } else if (usuario.getTipoUsuario() == Usuario.TipoUsuario.PROFESIONAL) {
            UI.getCurrent().navigate("dashboard-profesional");
        }
    }
}
