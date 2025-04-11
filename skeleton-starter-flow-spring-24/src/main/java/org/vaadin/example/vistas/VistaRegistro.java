package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.models.Usuario;

import org.springframework.beans.factory.annotation.Autowired;

@Route("registro")
@PageTitle("Registro | Reclaimers")
public class VistaRegistro extends VerticalLayout {

    @Autowired
    private ServicioUsuario servicioUsuario;

    public VistaRegistro() {

        TextField nombreField = new TextField("Nombre");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Contraseña");

        // Campo para la contraseña especial para los profesionales
        TextField passwordProfesionalField = new TextField("Contraseña Profesional (opcional)");

        Button registerButton = new Button("Registrarse");
        Button volverButton = new Button("Volver a Inicio", event -> getUI().ifPresent(ui -> ui.navigate("")));

        registerButton.addClickListener(event -> {
            String nombre = nombreField.getValue();  // Asegúrate de que el valor del nombre se extrae del campo nombreField
            String email = emailField.getValue();
            String contrasena = passwordField.getValue();
            String contrasenaProfesional = passwordProfesionalField.getValue();

            // Imprimir los valores en la consola para depuración
            System.out.println("Nombre: " + nombre);
            System.out.println("Email: " + email);
            System.out.println("Contraseña: " + contrasena);

            // Validar que el nombre no esté vacío
            if (nombre == null || nombre.trim().isEmpty()) {
                Notification.show("El nombre no puede estar vacío", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Validar que el email no esté vacío
            if (email == null || email.trim().isEmpty()) {
                Notification.show("El email no puede estar vacío", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Validar que la contraseña no esté vacía
            if (contrasena == null || contrasena.trim().isEmpty()) {
                Notification.show("La contraseña no puede estar vacía", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Determinar si es un profesional basándonos en la contraseña especial
            boolean esProfesional = contrasenaProfesional.equals("TuContraseñaEspecial");

            // Crear el objeto Usuario con todos los valores correctos
            Usuario usuario = new Usuario(nombre, email, contrasena, esProfesional ? Usuario.TipoUsuario.PROFESIONAL : Usuario.TipoUsuario.LUDOPATA);

            // Llamar al servicio para registrar el usuario
            String response = servicioUsuario.registrarUsuario(usuario);

            // Procesamos los errores y mostramos solo los mensajes sin las claves
            if (response.contains("La contraseña debe tener al menos 6 caracteres") ||
                    response.contains("El email no es válido") ||
                    response.contains("El correo ya está registrado")) {

                // Extraer solo el mensaje de error sin la clave
                String message = response.split(":")[1].trim();
                Notification.show(message, 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show("Usuario registrado correctamente", 3000, Notification.Position.MIDDLE);
                // Redirigir al login
                getUI().ifPresent(ui -> ui.navigate("inicio-sesion"));
            }
        });

        add(new H1("Reclaimers - Registro"), nombreField, emailField, passwordField, passwordProfesionalField, registerButton, volverButton);
        setAlignItems(Alignment.CENTER);
    }
}
