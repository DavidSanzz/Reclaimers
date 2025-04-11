package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Inicio | Reclaimers")
public class VistaInicio extends VerticalLayout {

    public VistaInicio() {
        H1 titulo = new H1("Bienvenido a Reclaimers");
        Paragraph descripcion = new Paragraph("Nuestra misión es ayudarte a superar la ludopatía con herramientas y apoyo especializado.");

        Button loginButton = new Button("Iniciar Sesión", event ->
                getUI().ifPresent(ui -> ui.navigate("inicio-sesion")));

        Button registerButton = new Button("Registrarse", event ->
                getUI().ifPresent(ui -> ui.navigate("registro")));

        loginButton.getStyle().set("margin-right", "10px");

        add(titulo, descripcion, loginButton, registerButton);
        setAlignItems(Alignment.CENTER);
    }
}
