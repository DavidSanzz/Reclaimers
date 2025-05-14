package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Mensaje;
import org.vaadin.example.models.Usuario;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.dependency.CssImport;

@CssImport("./styles/styles.css")
@Route("mensajes")
@PageTitle("Mensajes | RESET")
public class VistaMensajesLudopata extends VerticalLayout {

    private final ServicioUsuario servicioUsuario;
    private final Usuario usuarioActual;
    private final Grid<Mensaje> gridMensajes = new Grid<>(Mensaje.class, false);
    private Map<Long, String> mapaProfesionales = new HashMap<>();

    @Autowired
    public VistaMensajesLudopata(ServicioUsuario servicioUsuario) {
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

        add(new H1("Mensajes con profesionales"));
        add(new H2("Envía y recibe mensajes de forma segura"));

        ComboBox<Usuario> comboProfesionales = new ComboBox<>("Seleccionar profesional");
        comboProfesionales.setItemLabelGenerator(Usuario::getNombre);
        comboProfesionales.setWidth("400px");

        List<Usuario> listaProfesionales = servicioUsuario.obtenerProfesionales();
        comboProfesionales.setItems(listaProfesionales);

        listaProfesionales.forEach(pro -> mapaProfesionales.put(pro.getId(), pro.getNombre()));

        TextArea contenidoField = new TextArea("Contenido del mensaje");
        contenidoField.setWidth("400px");
        contenidoField.setHeight("120px");

        Button enviarButton = new Button("Enviar mensaje");
        enviarButton.addClassName("boton-principal");

        enviarButton.addClickListener(event -> {
            Usuario profesionalSeleccionado = comboProfesionales.getValue();
            String contenido = contenidoField.getValue().trim();

            if (profesionalSeleccionado == null || contenido.isEmpty()) {
                Notification.show("Selecciona un profesional y escribe un mensaje", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (profesionalSeleccionado.getId().equals(usuarioActual.getId())) {
                Notification.show("No puedes enviarte un mensaje a ti mismo", 3000, Notification.Position.MIDDLE);
                return;
            }

            Mensaje nuevo = new Mensaje(usuarioActual.getId(), profesionalSeleccionado.getId(), contenido);
            servicioUsuario.enviarMensaje(nuevo);

            Notification.show("Mensaje enviado a " + profesionalSeleccionado.getNombre(), 3000, Notification.Position.MIDDLE);
            contenidoField.clear();
            cargarMensajes();
        });

        VerticalLayout envioLayout = new VerticalLayout(
                new H3("Enviar nuevo mensaje"),
                comboProfesionales,
                contenidoField,
                enviarButton
        );
        envioLayout.addClassName("seccion");
        add(envioLayout);

        gridMensajes.addColumn(mensaje ->
                        mensaje.getEmisorId().equals(usuarioActual.getId()) ? "Yo" :
                                mapaProfesionales.getOrDefault(mensaje.getEmisorId(), "Desconocido"))
                .setHeader("Remitente");

        gridMensajes.addColumn(mensaje ->
                        mensaje.getReceptorId().equals(usuarioActual.getId()) ? "Yo" :
                                mapaProfesionales.getOrDefault(mensaje.getReceptorId(), "Desconocido"))
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

        // === Botón fijo de volver ===
        Button volverButton = new Button("← Volver al panel");
        volverButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("mi-espacio")));
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
}
