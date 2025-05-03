package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;

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

        setAlignItems(Alignment.CENTER);

        if (usuarioActual != null) {
            H1 welcomeMessage = new H1("Bienvenido, " + usuarioActual.getNombre());
            add(welcomeMessage);
        } else {
            Notification.show("No se pudo obtener la información del usuario", 3000, Notification.Position.MIDDLE);
            return;
        }

        // ---- SECCIÓN: SEGUIMIENTOS ----
        add(new H2("Registrar seguimiento semanal"));

        TextArea seguimientoComentario = new TextArea("Comentario");
        TextArea seguimientoProgreso = new TextArea("Progreso");
        Button saveButton = new Button("Guardar Seguimiento");

        saveButton.addClickListener(event -> {
            String comentario = seguimientoComentario.getValue();
            String progreso = seguimientoProgreso.getValue();

            SeguimientoProgreso seguimiento = new SeguimientoProgreso(comentario, progreso, usuarioActual);
            String response = servicioUsuario.guardarSeguimiento(seguimiento);
            Notification.show(response, 3000, Notification.Position.MIDDLE);

            if (response.contains("correctamente")) {
                cargarSeguimientos();
                seguimientoComentario.clear();
                seguimientoProgreso.clear();
            }
        });

        add(seguimientoComentario, seguimientoProgreso, saveButton);

        // ---- TABLA DE SEGUIMIENTOS ----
        gridSeguimientos = new Grid<>(SeguimientoProgreso.class);
        gridSeguimientos.setColumns("comentario", "progreso");
        gridSeguimientos.setAllRowsVisible(true);
        gridSeguimientos.setWidth("80%");
        add(new H2("Seguimientos anteriores"), gridSeguimientos);

        cargarSeguimientos();

        // ---- SECCIÓN: INFORMACIÓN ADICIONAL ----
        add(new H2("Actualizar información personal sobre la ludopatía"));

        TextField tipoJuegoField = new TextField("Tipo de juego favorito");
        TextField horarioJuegoField = new TextField("Horario frecuente de juego");
        TextField tiempoSemanalField = new TextField("Horas de juego por semana");
        TextField dineroGastadoField = new TextField("Dinero gastado mensual (€)");
        ComboBox<Usuario.NivelAutoevaluacion> nivelAutoevaluacionCombo = new ComboBox<>("Nivel de autoevaluación");
        nivelAutoevaluacionCombo.setItems(Usuario.NivelAutoevaluacion.values());
        TextArea objetivoField = new TextArea("Objetivo personal");

        // Precargar datos si existen
        if (usuarioActual.getTipoJuegoFavorito() != null) tipoJuegoField.setValue(usuarioActual.getTipoJuegoFavorito());
        if (usuarioActual.getHorarioFrecuenteJuego() != null) horarioJuegoField.setValue(usuarioActual.getHorarioFrecuenteJuego());
        if (usuarioActual.getTiempoSemanalJuego() != null) tiempoSemanalField.setValue(usuarioActual.getTiempoSemanalJuego().toString());
        if (usuarioActual.getDineroGastadoMensual() != null) dineroGastadoField.setValue(usuarioActual.getDineroGastadoMensual().toString());
        if (usuarioActual.getNivelAutoevaluacion() != null) nivelAutoevaluacionCombo.setValue(usuarioActual.getNivelAutoevaluacion());
        if (usuarioActual.getObjetivoPersonal() != null) objetivoField.setValue(usuarioActual.getObjetivoPersonal());

        Button actualizarInfoButton = new Button("Actualizar información");

        actualizarInfoButton.addClickListener(event -> {
            Usuario datosActualizados = new Usuario();

            if (!tipoJuegoField.isEmpty()) datosActualizados.setTipoJuegoFavorito(tipoJuegoField.getValue());
            if (!horarioJuegoField.isEmpty()) datosActualizados.setHorarioFrecuenteJuego(horarioJuegoField.getValue());

            if (!tiempoSemanalField.isEmpty()) {
                try {
                    datosActualizados.setTiempoSemanalJuego(Integer.parseInt(tiempoSemanalField.getValue()));
                } catch (NumberFormatException e) {
                    Notification.show("El campo de horas semanales debe ser un número", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }

            if (!dineroGastadoField.isEmpty()) {
                try {
                    datosActualizados.setDineroGastadoMensual(Double.parseDouble(dineroGastadoField.getValue()));
                } catch (NumberFormatException e) {
                    Notification.show("El campo de dinero gastado debe ser un número", 3000, Notification.Position.MIDDLE);
                    return;
                }
            }

            if (nivelAutoevaluacionCombo.getValue() != null)
                datosActualizados.setNivelAutoevaluacion(nivelAutoevaluacionCombo.getValue());

            if (!objetivoField.isEmpty()) datosActualizados.setObjetivoPersonal(objetivoField.getValue());

            String respuesta = servicioUsuario.actualizarInformacionAdicional(usuarioActual.getId(), datosActualizados);
            Notification.show(respuesta, 3000, Notification.Position.MIDDLE);

            // Actualizar el objeto usuario en sesión tras los cambios
            // Reflejar localmente los cambios en el objeto de sesión
            if (datosActualizados.getTipoJuegoFavorito() != null)
                usuarioActual.setTipoJuegoFavorito(datosActualizados.getTipoJuegoFavorito());
            if (datosActualizados.getHorarioFrecuenteJuego() != null)
                usuarioActual.setHorarioFrecuenteJuego(datosActualizados.getHorarioFrecuenteJuego());
            if (datosActualizados.getTiempoSemanalJuego() != null)
                usuarioActual.setTiempoSemanalJuego(datosActualizados.getTiempoSemanalJuego());
            if (datosActualizados.getDineroGastadoMensual() != null)
                usuarioActual.setDineroGastadoMensual(datosActualizados.getDineroGastadoMensual());
            if (datosActualizados.getNivelAutoevaluacion() != null)
                usuarioActual.setNivelAutoevaluacion(datosActualizados.getNivelAutoevaluacion());
            if (datosActualizados.getObjetivoPersonal() != null)
                usuarioActual.setObjetivoPersonal(datosActualizados.getObjetivoPersonal());

            UsuarioSesion.setUsuario(usuarioActual);

        });

        add(tipoJuegoField, horarioJuegoField, tiempoSemanalField, dineroGastadoField,
                nivelAutoevaluacionCombo, objetivoField, actualizarInfoButton);
    }

    private void cargarSeguimientos() {
        List<SeguimientoProgreso> seguimientos = servicioUsuario.obtenerSeguimientosPorUsuario(usuarioActual.getId());
        gridSeguimientos.setItems(seguimientos);
    }
}
