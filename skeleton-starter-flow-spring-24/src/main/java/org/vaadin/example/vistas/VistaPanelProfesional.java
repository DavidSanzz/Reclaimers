package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
<<<<<<< HEAD
import com.vaadin.flow.component.grid.GridVariant;
=======
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
<<<<<<< HEAD
import com.vaadin.flow.component.dependency.CssImport;

import org.springframework.web.client.RestTemplate;
import org.vaadin.example.UsuarioSesion;
=======

import org.springframework.web.client.RestTemplate;
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
import org.vaadin.example.models.Recurso;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Paragraph;

@CssImport("./styles/styles.css")
@Route("panel-profesional")
@PageTitle("Panel Profesional | RESET")
public class VistaPanelProfesional extends VerticalLayout {

    private Grid<SeguimientoProgreso> grid = new Grid<>(SeguimientoProgreso.class);
    private Usuario usuarioActual;

    public VistaPanelProfesional() {
        this.usuarioActual = UsuarioSesion.getUsuario();

        addClassName("vista-paneles");
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setWidthFull();

        if (usuarioActual == null) {
            Notification.show("No se pudo obtener la información del usuario", 3000, Notification.Position.MIDDLE);
            return;
        }

        // BOTÓN FLOTANTE DE MENSAJERÍA
        Button botonMensajeria = new Button("💬");
        botonMensajeria.addClassName("boton-mensajeria-flotante");
        botonMensajeria.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("mensajes-profesional")));
        add(botonMensajeria);

        // Bienvenida personalizada
        VerticalLayout bienvenida = new VerticalLayout(
                new H1("Panel del Profesional"),
                new H2("Bienvenido, " + usuarioActual.getNombre())
        );
        bienvenida.addClassName("seccion");
        add(bienvenida);

        VerticalLayout layoutSeguimientos = new VerticalLayout();
        layoutSeguimientos.addClassName("seccion");
        layoutSeguimientos.add(new H2("Seguimientos de pacientes"));

        configurarGrid();
        cargarSeguimientos();

<<<<<<< HEAD
        layoutSeguimientos.add(grid);
        add(layoutSeguimientos);

        mostrarFormularioSubidaRecurso();

        // --- Diálogo de confirmación ---
        Dialog dialogoConfirmacion = new Dialog();
        dialogoConfirmacion.setHeaderTitle("¿Cerrar sesión?");
        dialogoConfirmacion.add(new Paragraph("¿Estás seguro de que quieres cerrar sesión?"));

        // Botones del diálogo
        Button confirmar = new Button("Sí, cerrar", event -> {
            UsuarioSesion.setUsuario(null);
            getUI().ifPresent(ui -> ui.navigate("")); // Vista raíz = bienvenida
            dialogoConfirmacion.close();
        });
        confirmar.getStyle().set("background-color", "#c9302c").set("color", "white");

        Button cancelar = new Button("Cancelar", event -> dialogoConfirmacion.close());

        HorizontalLayout botonesDialogo = new HorizontalLayout(cancelar, confirmar);
        dialogoConfirmacion.getFooter().add(botonesDialogo);

        // --- Botón flotante para cerrar sesión ---
        Button cerrarSesion = new Button("Cerrar sesión");
        cerrarSesion.addClassName("boton-cerrar-sesion-flotante");
        cerrarSesion.addClickListener(e -> dialogoConfirmacion.open());

        add(dialogoConfirmacion, cerrarSesion);

=======
        add(subtitulo, grid);
        mostrarFormularioSubidaRecurso();
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
    }

    private void configurarGrid() {
        grid.removeAllColumns();

        grid.addColumn(seguimiento -> {
            Usuario u = seguimiento.getUsuario();
            return u != null ? u.getNombre() : "Desconocido";
        }).setHeader("Paciente");

        grid.addColumn(SeguimientoProgreso::getComentario).setHeader("Comentario");
        grid.addColumn(SeguimientoProgreso::getProgreso).setHeader("Progreso");

        grid.addColumn(seguimiento -> {
            if (seguimiento.getFecha() != null) {
                return seguimiento.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } else {
                return "Sin fecha";
            }
        }).setHeader("Fecha");

<<<<<<< HEAD
        grid.setAllRowsVisible(true);
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
=======
        grid.setHeight("275px");
        grid.setWidthFull();
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
    }

    private void cargarSeguimientos() {
        try {
            String url = "https://reset-app-q6h3.onrender.com/seguimiento/todos";
            RestTemplate restTemplate = new RestTemplate();
            SeguimientoProgreso[] datos = restTemplate.getForObject(url, SeguimientoProgreso[].class);
            grid.setItems(Arrays.asList(datos));

            grid.addItemClickListener(event -> {
                SeguimientoProgreso seguimientoSeleccionado = event.getItem();
                mostrarInformacionPaciente(seguimientoSeleccionado);
            });

        } catch (Exception e) {
            Notification.show("Error al cargar seguimientos: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void mostrarInformacionPaciente(SeguimientoProgreso seguimiento) {
        Usuario usuario = seguimiento.getUsuario();

        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("Información del paciente");

        Div contenido = new Div();
        contenido.add(
                new Paragraph("Nombre: " + usuario.getNombre()),
                new Paragraph("Email: " + usuario.getEmail()),
                new Paragraph("Tipo de juego favorito: " + valorSeguro(usuario.getTipoJuegoFavorito())),
                new Paragraph("Horario frecuente de juego: " + valorSeguro(usuario.getHorarioFrecuenteJuego())),
                new Paragraph("Tiempo semanal de juego: " + valorSeguro(usuario.getTiempoSemanalJuego()) + " horas"),
                new Paragraph("Dinero gastado mensual: " + valorSeguro(usuario.getDineroGastadoMensual()) + " €"),
                new Paragraph("Nivel de autoevaluación: " + valorSeguro(usuario.getNivelAutoevaluacion())),
                new Paragraph("Objetivo personal: " + valorSeguro(usuario.getObjetivoPersonal()))
        );

        dialogo.add(contenido);
        dialogo.getFooter().add(new Button("Cerrar", e -> dialogo.close()));
        dialogo.open();
    }

    private void mostrarFormularioSubidaRecurso() {
<<<<<<< HEAD
        VerticalLayout formularioLayout = new VerticalLayout();
        formularioLayout.addClassName("seccion");

=======
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
        H2 tituloFormulario = new H2("Publicar nuevo recurso educativo");

        TextField campoTitulo = new TextField("Título");
        TextArea campoDescripcion = new TextArea("Descripción");
        TextField campoTipo = new TextField("Tipo (PDF, artículo, video...)");
        TextField campoEnlace = new TextField("Enlace");

<<<<<<< HEAD
        campoTitulo.setWidth("400px");
        campoDescripcion.setWidth("400px");
        campoTipo.setWidth("400px");
        campoEnlace.setWidth("400px");

        Button botonPublicar = new Button("Publicar recurso");
        botonPublicar.addClassName("boton-principal");
=======
        // Tamano de los campos/componentes
        campoTitulo.setWidthFull();
        campoDescripcion.setWidthFull();
        campoTipo.setWidthFull();
        campoEnlace.setWidthFull();

        Button botonPublicar = new Button("Publicar recurso");
>>>>>>> 894d75a (Implementada funcionalidad para profesional)

        botonPublicar.addClickListener(e -> {
            if (campoTitulo.isEmpty() || campoEnlace.isEmpty()) {
                Notification.show("El título y el enlace son obligatorios.", 3000, Notification.Position.MIDDLE);
                return;
            }

            Recurso recurso = new Recurso();
            recurso.setTitulo(campoTitulo.getValue());
            recurso.setDescripcion(campoDescripcion.getValue());
            recurso.setTipo(campoTipo.getValue());
            recurso.setEnlace(campoEnlace.getValue());

            Usuario profesional = new Usuario();
<<<<<<< HEAD
            profesional.setId(usuarioActual.getId());
=======
            profesional.setId(2L); // Reemplazar por el ID real del profesional logueado
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
            recurso.setUsuarioProfesional(profesional);

            try {
                RestTemplate restTemplate = new RestTemplate();
<<<<<<< HEAD
                restTemplate.postForObject("https://reset-app-q6h3.onrender.com/recursos", recurso, Recurso.class);
=======
                restTemplate.postForObject("http://localhost:8081/recursos", recurso, Recurso.class);
>>>>>>> 894d75a (Implementada funcionalidad para profesional)

                Notification notification = Notification.show("Recurso publicado correctamente");
                notification.setPosition(Notification.Position.MIDDLE);
                notification.setDuration(3000);

                campoTitulo.clear();
                campoDescripcion.clear();
                campoTipo.clear();
                campoEnlace.clear();

            } catch (Exception ex) {
                Notification.show("Error al publicar el recurso.", 5000, Notification.Position.MIDDLE);
            }
        });

<<<<<<< HEAD
        formularioLayout.add(tituloFormulario, campoTitulo, campoDescripcion, campoTipo, campoEnlace, botonPublicar);
        add(formularioLayout);
=======
        VerticalLayout formulario = new VerticalLayout(
                tituloFormulario, campoTitulo, campoDescripcion, campoTipo, campoEnlace, botonPublicar
        );
        formulario.setSpacing(true);
        formulario.setPadding(true);
        formulario.setAlignItems(Alignment.CENTER); // Centramos los elementos dentro del recuadro
        formulario.setWidth("50%");
        formulario.getStyle()
                .set("border", "1px solid lightgray")
                .set("padding", "1rem")
                .set("border-radius", "8px")
                .set("margin-top", "2rem");

        Div contenedorFormulario = new Div(formulario);
        contenedorFormulario.getStyle()
                .set("display", "flex")
                .set("justify-content", "center")
                .set("width", "100%");

        add(contenedorFormulario);
>>>>>>> 894d75a (Implementada funcionalidad para profesional)
    }

    private String valorSeguro(Object valor) {
        return valor != null ? valor.toString() : "No especificado";
    }
}
