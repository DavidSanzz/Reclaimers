package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vaadin.example.ServicioUsuario;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Recurso;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;

import java.util.List;
import java.util.Arrays;

import com.vaadin.flow.component.dependency.CssImport;
@CssImport("./styles/styles.css")

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

        addClassName("vista-ludopata-fondo");

        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setSpacing(true);
        setPadding(true);

        if (usuarioActual == null) {
            Notification.show("No se pudo obtener la información del usuario", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Bienvenida
        VerticalLayout bienvenida = new VerticalLayout(new H1("Bienvenido, " + usuarioActual.getNombre()));
        bienvenida.addClassName("seccion");
        add(bienvenida);

        // Seguimiento semanal
        TextArea seguimientoComentario = new TextArea("Comentario");
        TextArea seguimientoProgreso = new TextArea("Progreso");
        seguimientoComentario.setWidth("400px");
        seguimientoProgreso.setWidth("400px");

        Button saveButton = new Button("Guardar Seguimiento");
        saveButton.addClassName("boton-principal");

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

        VerticalLayout seguimientoLayout = new VerticalLayout(
                new H2("Registrar seguimiento semanal"),
                seguimientoComentario, seguimientoProgreso, saveButton
        );
        seguimientoLayout.addClassName("seccion");
        add(seguimientoLayout);

        // Tabla de seguimientos anteriores
        gridSeguimientos = new Grid<>(SeguimientoProgreso.class, false);

        gridSeguimientos.addColumn(SeguimientoProgreso::getComentario)
                .setHeader("Comentario")
                .setAutoWidth(true)
                .setFlexGrow(0);

        gridSeguimientos.addColumn(SeguimientoProgreso::getProgreso)
                .setHeader("Progreso")
                .setAutoWidth(true)
                .setFlexGrow(0);

        gridSeguimientos.addColumn(seguimiento -> {
                    if (seguimiento.getFecha() != null) {
                        return seguimiento.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    } else {
                        return "Sin fecha";
                    }
                }).setHeader("Fecha")
                .setAutoWidth(true)
                .setFlexGrow(0);

        gridSeguimientos.setAllRowsVisible(true);
        gridSeguimientos.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);

        VerticalLayout tablaContainer = new VerticalLayout(new H2("Seguimientos anteriores"), gridSeguimientos);
        tablaContainer.addClassName("seccion");
        add(tablaContainer);
        cargarSeguimientos();

        // Información personal
        TextField tipoJuegoField = new TextField("Tipo de juego favorito");
        TextField horarioJuegoField = new TextField("Horario frecuente de juego");
        TextField tiempoSemanalField = new TextField("Horas de juego por semana");
        TextField dineroGastadoField = new TextField("Dinero gastado mensual (€)");
        ComboBox<Usuario.NivelAutoevaluacion> nivelAutoevaluacionCombo = new ComboBox<>("Nivel de autoevaluación");
        nivelAutoevaluacionCombo.setItems(Usuario.NivelAutoevaluacion.values());
        TextArea objetivoField = new TextArea("Objetivo personal");

        tipoJuegoField.setWidth("400px");
        horarioJuegoField.setWidth("400px");
        tiempoSemanalField.setWidth("400px");
        dineroGastadoField.setWidth("400px");
        nivelAutoevaluacionCombo.setWidth("400px");
        objetivoField.setWidth("400px");

        if (usuarioActual.getTipoJuegoFavorito() != null) tipoJuegoField.setValue(usuarioActual.getTipoJuegoFavorito());
        if (usuarioActual.getHorarioFrecuenteJuego() != null) horarioJuegoField.setValue(usuarioActual.getHorarioFrecuenteJuego());
        if (usuarioActual.getTiempoSemanalJuego() != null) tiempoSemanalField.setValue(usuarioActual.getTiempoSemanalJuego().toString());
        if (usuarioActual.getDineroGastadoMensual() != null) dineroGastadoField.setValue(usuarioActual.getDineroGastadoMensual().toString());
        if (usuarioActual.getNivelAutoevaluacion() != null) nivelAutoevaluacionCombo.setValue(usuarioActual.getNivelAutoevaluacion());
        if (usuarioActual.getObjetivoPersonal() != null) objetivoField.setValue(usuarioActual.getObjetivoPersonal());

        Button actualizarInfoButton = new Button("Actualizar información");
        actualizarInfoButton.addClassName("boton-principal");

        actualizarInfoButton.addClickListener(event -> {
            Usuario datosActualizados = new Usuario();

            if (!tipoJuegoField.isEmpty()) datosActualizados.setTipoJuegoFavorito(tipoJuegoField.getValue());
            if (!horarioJuegoField.isEmpty()) datosActualizados.setHorarioFrecuenteJuego(horarioJuegoField.getValue());

            try {
                if (!tiempoSemanalField.isEmpty()) {
                    datosActualizados.setTiempoSemanalJuego(Integer.parseInt(tiempoSemanalField.getValue()));
                }
                if (!dineroGastadoField.isEmpty()) {
                    datosActualizados.setDineroGastadoMensual(Double.parseDouble(dineroGastadoField.getValue()));
                }
            } catch (NumberFormatException e) {
                Notification.show("Revisa los campos numéricos", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (nivelAutoevaluacionCombo.getValue() != null)
                datosActualizados.setNivelAutoevaluacion(nivelAutoevaluacionCombo.getValue());
            if (!objetivoField.isEmpty())
                datosActualizados.setObjetivoPersonal(objetivoField.getValue());

            String respuesta = servicioUsuario.actualizarInformacionAdicional(usuarioActual.getId(), datosActualizados);
            Notification.show(respuesta, 3000, Notification.Position.MIDDLE);

            // Refrescar usuario en sesión
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

        VerticalLayout infoLayout = new VerticalLayout(
                new H2("Actualizar información personal sobre la ludopatía"),
                tipoJuegoField, horarioJuegoField, tiempoSemanalField,
                dineroGastadoField, nivelAutoevaluacionCombo, objetivoField,
                actualizarInfoButton
        );
        infoLayout.addClassName("seccion");
        add(infoLayout);

        // Enviar informe PDF
        TextField correoDestino = new TextField("Correo electrónico de destino");
        correoDestino.setPlaceholder("ejemplo@correo.com");
        correoDestino.setWidth("400px");

        Button botonEnviarInforme = new Button("Enviar informe PDF");
        botonEnviarInforme.addClassName("boton-principal");

        botonEnviarInforme.addClickListener(e -> {
            String correo = correoDestino.getValue().trim();
            if (correo.isEmpty() || !correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                Notification.show("Introduce un correo válido", 3000, Notification.Position.MIDDLE);
                return;
            }
            String resultado = servicioUsuario.enviarInformePdf(usuarioActual.getId(), correo);
            Notification.show(resultado, 4000, Notification.Position.MIDDLE);
        });

        VerticalLayout informeLayout = new VerticalLayout(
                new H2("Enviar informe en PDF por correo"),
                correoDestino, botonEnviarInforme
        );
        informeLayout.addClassName("seccion");
        add(informeLayout);

        // Recursos educativos
        Grid<Recurso> gridRecursos = new Grid<>(Recurso.class, false);
        gridRecursos.addColumn(Recurso::getTitulo).setHeader("Título").setAutoWidth(true);
        gridRecursos.addColumn(Recurso::getTipo).setHeader("Tipo").setAutoWidth(true);
        gridRecursos.addColumn(recurso -> {
            if (recurso.getUsuarioProfesional() != null && recurso.getUsuarioProfesional().getNombre() != null) {
                return recurso.getUsuarioProfesional().getNombre();
            } else {
                return "Desconocido";
            }
        }).setHeader("Publicado por").setAutoWidth(true);

        gridRecursos.addComponentColumn(recurso -> {
                    Button ver = new Button("Ver recurso");
                    ver.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().open(recurso.getEnlace())));
                    return ver;
                })
                .setHeader("Acción")
                .setAutoWidth(true)
                .setFlexGrow(0);

        gridRecursos.setAllRowsVisible(true);
        gridRecursos.setWidthFull();
        gridRecursos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        try {
            RestTemplate restTemplate = new RestTemplate();
            Recurso[] recursos = restTemplate.getForObject("http://localhost:8081/recursos", Recurso[].class);
            gridRecursos.setItems(Arrays.asList(recursos));
        } catch (Exception e) {
            Notification.show("Error al cargar recursos educativos: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }

        VerticalLayout recursosLayout = new VerticalLayout(
                new H2("Recursos educativos disponibles"),
                gridRecursos
        );
        recursosLayout.addClassName("seccion");
        add(recursosLayout);
    }

    private void cargarSeguimientos() {
        List<SeguimientoProgreso> seguimientos = servicioUsuario.obtenerSeguimientosPorUsuario(usuarioActual.getId());
        gridSeguimientos.setItems(seguimientos);
    }
}
