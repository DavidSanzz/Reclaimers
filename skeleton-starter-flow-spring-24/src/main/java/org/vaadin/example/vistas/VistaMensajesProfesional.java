package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Mensaje;
import org.vaadin.example.models.Usuario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CssImport("./styles/styles.css")
@Route("mensajes-profesional")
@PageTitle("Mensajes | RESET")
public class VistaMensajesProfesional extends VerticalLayout {

    private final ServicioUsuario servicioUsuario;
    private final Usuario usuarioActual;
    private final Grid<Mensaje> gridMensajes = new Grid<>(Mensaje.class, false);
    private Map<Long, String> mapaPacientes = new HashMap<>();

    @Autowired
    public VistaMensajesProfesional(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
        this.usuarioActual = UsuarioSesion.getUsuario();

        addClassName("vista-paneles");
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setSpacing(true);

        if (usuarioActual == null) {
            Notification.show("Error: No se ha detectado una sesión activa", 3000, Notification.Position.MIDDLE);
            return;
        }

        add(new H1("Mensajes con pacientes"));
        add(new H2("Responde y gestiona mensajes de tus pacientes"));

        // ComboBox de pacientes
        ComboBox<Usuario> comboPacientes = new ComboBox<>("Seleccionar paciente");
        comboPacientes.setItemLabelGenerator(Usuario::getNombre);
        comboPacientes.setWidth("400px");

        // Limitar altura del desplegable a unas 6–7 filas
        comboPacientes.getElement().executeJs("this.$.overlay.style.maxHeight = '250px';");

        List<Usuario> pacientes = servicioUsuario.obtenerPacientes();
        comboPacientes.setItems(pacientes);
        pacientes.forEach(p -> mapaPacientes.put(p.getId(), p.getNombre()));


        // Campo de mensaje
        TextArea contenidoField = new TextArea("Contenido del mensaje");
        contenidoField.setWidth("400px");
        contenidoField.setHeight("120px");

        Button enviarButton = new Button("Enviar mensaje");
        enviarButton.addClassName("boton-principal");

        enviarButton.addClickListener(event -> {
            Usuario pacienteSeleccionado = comboPacientes.getValue();
            String contenido = contenidoField.getValue().trim();

            if (pacienteSeleccionado == null || contenido.isEmpty()) {
                Notification.show("Selecciona un paciente y escribe un mensaje", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (pacienteSeleccionado.getId().equals(usuarioActual.getId())) {
                Notification.show("No puedes enviarte un mensaje a ti mismo", 3000, Notification.Position.MIDDLE);
                return;
            }

            Mensaje nuevo = new Mensaje(usuarioActual.getId(), pacienteSeleccionado.getId(), contenido);
            servicioUsuario.enviarMensaje(nuevo);

            Notification.show("Mensaje enviado a " + pacienteSeleccionado.getNombre(), 3000, Notification.Position.MIDDLE);
            contenidoField.clear();
            cargarMensajes();
        });

        VerticalLayout envioLayout = new VerticalLayout(
                new H3("Enviar nuevo mensaje"),
                comboPacientes,
                contenidoField,
                enviarButton
        );
        envioLayout.addClassName("seccion");
        add(envioLayout);

        // Tabla de mensajes
        gridMensajes.addColumn(mensaje -> {
                    if (mensaje.getEmisorId().equals(usuarioActual.getId())) {
                        return "Yo";
                    } else {
                        return buscarNombreUsuario(mensaje.getEmisorId());
                    }
                })
                .setHeader("Remitente");

        gridMensajes.addColumn(mensaje -> {
                    if (mensaje.getReceptorId().equals(usuarioActual.getId())) {
                        return "Yo";
                    } else {
                        return buscarNombreUsuario(mensaje.getReceptorId());
                    }
                })
                .setHeader("Destinatario");

        gridMensajes.addColumn(Mensaje::getContenido).setHeader("Mensaje");

        gridMensajes.addColumn(mensaje -> {
            try {
                LocalDateTime fecha = LocalDateTime.parse(mensaje.getFechaEnvio());
                return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (Exception e) {
                return mensaje.getFechaEnvio();
            }
        }).setHeader("Fecha").setAutoWidth(true).setFlexGrow(0);

        gridMensajes.setAllRowsVisible(true);
        gridMensajes.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridMensajes.setWidthFull();
        gridMensajes.setMaxWidth("800px");

        VerticalLayout gridContainer = new VerticalLayout(
                new H3("Historial de mensajes"),
                gridMensajes
        );
        gridContainer.addClassName("seccion");
        add(gridContainer);

        // Botón volver
        Button volverButton = new Button("← Volver al panel");
        volverButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("dashboard-profesional")));
        volverButton.addClassName("boton-volver-flotante");
        add(volverButton);

        cargarMensajes();
    }

    private void cargarMensajes() {
        try {
            List<Mensaje> mensajes = servicioUsuario.obtenerMensajesDeUsuario(usuarioActual.getId());
            gridMensajes.setItems(mensajes);
        } catch (Exception e) {
            Notification.show("No se pudieron cargar los mensajes", 3000, Notification.Position.MIDDLE);
        }
    }

    private String buscarNombreUsuario(Long id) {
        return mapaPacientes.getOrDefault(id, "Desconocido");
    }
}
