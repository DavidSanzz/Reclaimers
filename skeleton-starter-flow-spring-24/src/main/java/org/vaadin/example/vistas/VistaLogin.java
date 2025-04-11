package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import java.util.Map;
import org.vaadin.example.ServicioUsuario;
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

            // Verificar si los campos están vacíos
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                // Verificar si uno o ambos campos están vacíos y mostrar el error correspondiente
                if (email.trim().isEmpty() && password.trim().isEmpty()) {
                    Notification.show("El correo y la contraseña no pueden estar vacíos", 3000, Notification.Position.MIDDLE);
                } else if (email.trim().isEmpty()) {
                    Notification.show("El correo no puede estar vacío", 3000, Notification.Position.MIDDLE);
                } else if (password.trim().isEmpty()) {
                    Notification.show("La contraseña no puede estar vacía", 3000, Notification.Position.MIDDLE);
                }
                return; // No enviar solicitud si hay campos vacíos
            }

            realizarLogin(email, password); // Llamar a la función si los campos no están vacíos
        });

        add(new H1("Reclaimers - Iniciar Sesión"), emailField, passwordField, loginButton, volverButton);
        setAlignItems(Alignment.CENTER);
    }

    private void realizarLogin(String email, String password) {
        Map<String, Object> response = servicioUsuario.login(email, password);

        // Verificar el campo "status"
        String status = (String) response.get("status");
        String message = (String) response.get("message");

        if ("error".equals(status)) {
            Notification.show(message, 3000, Notification.Position.MIDDLE);
            return; // No continuar si hay un error
        }

        // Si el login es exitoso, redirigir según tipo de usuario
        if (response.containsKey("tipoUsuario")) {
            String tipoUsuario = (String) response.get("tipoUsuario");
            if ("LUDOPATA".equals(tipoUsuario)) {
                getUI().ifPresent(ui -> ui.navigate("dashboard-ludopata"));
            } else if ("PROFESIONAL".equals(tipoUsuario)) {
                getUI().ifPresent(ui -> ui.navigate("dashboard-profesional"));
            }
        }
    }
}

