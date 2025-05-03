package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.UI;

import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

@Route("inicio-sesion")
@PageTitle("Iniciar Sesión | Reclaimers")
public class VistaLogin extends VerticalLayout {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public VistaLogin(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;

        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Contraseña");
        Button loginButton = new Button("Iniciar sesión");
        Button volverButton = new Button("Volver a Inicio", event -> getUI().ifPresent(ui -> ui.navigate("")));

        loginButton.addClickListener(event -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                if (email.trim().isEmpty() && password.trim().isEmpty()) {
                    Notification.show("El correo y la contraseña no pueden estar vacíos", 3000, Notification.Position.MIDDLE);
                } else if (email.trim().isEmpty()) {
                    Notification.show("El correo no puede estar vacío", 3000, Notification.Position.MIDDLE);
                } else {
                    Notification.show("La contraseña no puede estar vacía", 3000, Notification.Position.MIDDLE);
                }
                return;
            }

            realizarLogin(email, password);
        });

        add(new H1("Reclaimers - Iniciar Sesión"), emailField, passwordField, loginButton, volverButton);
        setAlignItems(Alignment.CENTER);
    }

    private void realizarLogin(String email, String password) {
        Usuario usuario = servicioUsuario.loginYObtenerUsuario(email, password);

        if (usuario == null) {
            Notification.show("Correo o contraseña incorrectos o error de sesión", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Guardar en sesión
        UsuarioSesion.setUsuario(usuario);

        // Redirigir según tipo
        if (usuario.getTipoUsuario() == Usuario.TipoUsuario.LUDOPATA) {
            UI.getCurrent().navigate("dashboard-ludopata");
        } else if (usuario.getTipoUsuario() == Usuario.TipoUsuario.PROFESIONAL) {
            UI.getCurrent().navigate("dashboard-profesional");
        }
    }
}
