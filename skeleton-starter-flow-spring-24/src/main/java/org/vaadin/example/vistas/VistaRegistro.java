package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.models.Usuario;

@Route("registro")
@PageTitle("Registro | RESET")
public class VistaRegistro extends VerticalLayout {

    @Autowired
    private ServicioUsuario servicioUsuario;

    public VistaRegistro() {
        addClassName("vista-paneles");
        setAlignItems(Alignment.CENTER);
        setSpacing(true);

        H1 titulo = new H1("RESET - Registro");
        H3 subtitulo = new H3("Crea tu cuenta y comienza tu recuperación.");

        TextField nombreField = new TextField("Nombre");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Contraseña");
        TextField passwordProfesionalField = new TextField("Contraseña Profesional (opcional)");

        nombreField.setWidth("400px");
        emailField.setWidth("400px");
        passwordField.setWidth("400px");
        passwordProfesionalField.setWidth("400px");

        Button volverButton = new Button("Volver a Inicio", event -> getUI().ifPresent(ui -> ui.navigate("")));
        volverButton.addClassName("boton-principal");

        Button registerButton = new Button("Registrarse");
        registerButton.addClassName("boton-principal");

        registerButton.addClickListener(event -> {
            String nombre = nombreField.getValue();
            String email = emailField.getValue();
            String contrasena = passwordField.getValue();
            String contrasenaProfesional = passwordProfesionalField.getValue();

            if (nombre == null || nombre.trim().isEmpty()) {
                Notification.show("El nombre no puede estar vacío", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (email == null || email.trim().isEmpty()) {
                Notification.show("El email no puede estar vacío", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (contrasena == null || contrasena.trim().isEmpty()) {
                Notification.show("La contraseña no puede estar vacía", 3000, Notification.Position.MIDDLE);
                return;
            }

            boolean esProfesional = contrasenaProfesional.equals("TuContraseñaEspecial");
            Usuario usuario = new Usuario(nombre, email, contrasena,
                    esProfesional ? Usuario.TipoUsuario.PROFESIONAL : Usuario.TipoUsuario.LUDOPATA);

            String response = servicioUsuario.registrarUsuario(usuario);
            if (response.contains("La contraseña debe tener al menos 6 caracteres") ||
                    response.contains("El email no es válido") ||
                    response.contains("El correo ya está registrado")) {
                String message = response.split(":")[1].trim();
                Notification.show(message, 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show("Usuario registrado correctamente", 3000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate("inicio-sesion"));
            }
        });

        HorizontalLayout botonesLayout = new HorizontalLayout(volverButton, registerButton);
        botonesLayout.setWidth("400px");
        botonesLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VerticalLayout formulario = new VerticalLayout(
                titulo,
                subtitulo,
                nombreField,
                emailField,
                passwordField,
                passwordProfesionalField,
                botonesLayout
        );
        formulario.setAlignItems(Alignment.CENTER);
        formulario.addClassName("seccion");

        add(formulario);
    }
}
