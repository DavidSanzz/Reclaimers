package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.notification.Notification;

import java.util.List;

@Route("dashboard-ludopata")
@PageTitle("Dashboard Ludópata | Reclaimers")
public class VistaPanelLudopata extends VerticalLayout {

    private final ServicioUsuario servicioUsuario;
    private Usuario usuarioActual;

    private Grid<SeguimientoProgreso> gridSeguimientos;

    @Autowired
    public VistaPanelLudopata(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
        this.usuarioActual = UsuarioSesion.getUsuario();

        if (usuarioActual != null) {
            H1 welcomeMessage = new H1("Bienvenido, " + usuarioActual.getNombre());
            add(welcomeMessage);
        } else {
            Notification.show("No se pudo obtener la información del usuario", 3000, Notification.Position.MIDDLE);
            return;
        }

        TextArea seguimientoComentario = new TextArea("Comentario");
        TextArea seguimientoProgreso = new TextArea("Progreso");
        Button saveButton = new Button("Guardar Seguimiento");

        saveButton.addClickListener(event -> {
            String comentario = seguimientoComentario.getValue();
            String progreso = seguimientoProgreso.getValue();

            SeguimientoProgreso seguimiento = new SeguimientoProgreso(comentario, progreso, usuarioActual);

            String response = servicioUsuario.guardarSeguimiento(seguimiento);
            Notification.show(response, 3000, Notification.Position.MIDDLE);

            // Si se guarda correctamente, refrescar la tabla
            if (response.contains("correctamente")) {
                cargarSeguimientos();
                seguimientoComentario.clear();
                seguimientoProgreso.clear();
            }
        });

        add(seguimientoComentario, seguimientoProgreso, saveButton);

        // Tabla de seguimientos
        gridSeguimientos = new Grid<>(SeguimientoProgreso.class);
        gridSeguimientos.setColumns("comentario", "progreso");
        gridSeguimientos.setWidthFull();
        add(gridSeguimientos);

        // Cargar datos
        cargarSeguimientos();
    }

    private void cargarSeguimientos() {
        List<SeguimientoProgreso> seguimientos = servicioUsuario.obtenerSeguimientosPorUsuario(usuarioActual.getId());
        gridSeguimientos.setItems(seguimientos);
    }
}
