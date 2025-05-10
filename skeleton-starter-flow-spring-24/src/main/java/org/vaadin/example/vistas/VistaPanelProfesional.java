package org.vaadin.example.vistas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;

import org.springframework.web.client.RestTemplate;
import org.vaadin.example.UsuarioSesion;
import org.vaadin.example.models.Recurso;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@CssImport("./styles/styles.css")
@Route("dashboard-profesional")
@PageTitle("Dashboard Profesional | Reclaimers")
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

        layoutSeguimientos.add(grid);
        add(layoutSeguimientos);

        mostrarFormularioSubidaRecurso();
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

        grid.setAllRowsVisible(true);
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void cargarSeguimientos() {
        try {
            String url = "http://localhost:8081/seguimiento/todos";
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
        VerticalLayout formularioLayout = new VerticalLayout();
        formularioLayout.addClassName("seccion");

        H2 tituloFormulario = new H2("Publicar nuevo recurso educativo");

        TextField campoTitulo = new TextField("Título");
        TextArea campoDescripcion = new TextArea("Descripción");
        TextField campoTipo = new TextField("Tipo (PDF, artículo, video...)");
        TextField campoEnlace = new TextField("Enlace");

        campoTitulo.setWidth("400px");
        campoDescripcion.setWidth("400px");
        campoTipo.setWidth("400px");
        campoEnlace.setWidth("400px");

        Button botonPublicar = new Button("Publicar recurso");
        botonPublicar.addClassName("boton-principal");

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
            profesional.setId(usuarioActual.getId());
            recurso.setUsuarioProfesional(profesional);

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForObject("http://localhost:8081/recursos", recurso, Recurso.class);

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

        formularioLayout.add(tituloFormulario, campoTitulo, campoDescripcion, campoTipo, campoEnlace, botonPublicar);
        add(formularioLayout);
    }

    private String valorSeguro(Object valor) {
        return valor != null ? valor.toString() : "No especificado";
    }
}
