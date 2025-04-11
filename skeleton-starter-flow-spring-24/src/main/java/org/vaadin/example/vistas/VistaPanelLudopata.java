package org.vaadin.example.vistas;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;

@Route("dashboard-ludopata")
@PageTitle("Dashboard Ludópata | Reclaimers")
public class VistaPanelLudopata extends VerticalLayout {

    public VistaPanelLudopata() {
        add(new H1("Bienvenido al Dashboard de Ludópata"));
        setAlignItems(Alignment.CENTER);
    }
}
