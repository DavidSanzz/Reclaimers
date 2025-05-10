package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Image;

@Route("")
@PageTitle("Inicio | RESET")
@CssImport("./styles/styles.css")
public class VistaInicio extends VerticalLayout {

    public VistaInicio() {
        addClassName("vista-paneles");

        // Títulos principales
        H1 titulo = new H1("Bienvenido a RESET");
        H2 subtitulo = new H2("Recupera el control de tu vida. Estamos aquí para ayudarte.");

        // Descripción breve
        H3 queEs = new H3("¿Qué es RESET?");
        Paragraph descripcion = new Paragraph("RESET es una herramienta diseñada para apoyar la recuperación de personas con problemas de ludopatía, facilitando el seguimiento, la motivación y el acompañamiento profesional.");

        // Beneficios para pacientes
        H3 comoAyudamos = new H3("¿Cómo te ayudamos?");
        UnorderedList beneficios = new UnorderedList();
        beneficios.add(new ListItem("👤 Seguimiento de tu progreso semanal"));
        beneficios.add(new ListItem("📚 Acceso a recursos y consejos prácticos"));
        beneficios.add(new ListItem("📈 Visualización de tu evolución"));
        beneficios.add(new ListItem("💬 Contacto con profesionales especializados"));

        // Botones destacados
        Paragraph nuevo = new Paragraph("¿Nuevo en RESET?");

        Button registro = new Button("Comenzar mi recuperación", e -> getUI().ifPresent(ui -> ui.navigate("registro")));
        registro.addClassName("boton-principal");

        Button login = new Button("Acceder a mi cuenta", e -> getUI().ifPresent(ui -> ui.navigate("inicio-sesion")));
        login.addClassName("boton-principal");

        // Pie de página con logo y créditos
        Image logo = new Image("images/Reclaimers-removebg-preview.png", "Logo de Reclaimers");
        logo.setWidth("120px");

        Paragraph descripcionGrupo = new Paragraph("Somos RECLAIMERS, un grupo universitario comprometido con el desarrollo de soluciones digitales para combatir la ludopatía.");
        descripcionGrupo.getStyle().set("font-size", "0.9rem").set("color", "#666").set("text-align", "center");

        Paragraph footer = new Paragraph("© 2025 RECLAIMERS | Acabemos con la ludopatía juntos.");
        footer.getStyle().set("font-size", "0.8rem").set("color", "#999").set("margin-top", "2rem");

        // Estructura visual general
        add(
                titulo,
                subtitulo,
                queEs,
                descripcion,
                comoAyudamos,
                beneficios,
                nuevo,
                registro,
                login,
                new Hr(),
                logo,
                descripcionGrupo,
                footer
        );

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
    }
}