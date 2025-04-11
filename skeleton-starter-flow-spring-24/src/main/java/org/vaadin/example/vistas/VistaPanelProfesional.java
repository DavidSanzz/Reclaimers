package org.vaadin.example.vistas;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;

@Route("dashboard-profesional")
@PageTitle("Dashboard Profesional | Reclaimers")
public class VistaPanelProfesional extends VerticalLayout {

    public VistaPanelProfesional() {
        add(new H1("Bienvenido al Dashboard de Profesional"));
        setAlignItems(Alignment.CENTER);
    }
}