package org.vaadin.example.vistas;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.models.SeguimientoProgreso;
import org.vaadin.example.models.Usuario;
import org.springframework.web.client.RestTemplate;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.button.Button;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Route("dashboard-profesional")
@PageTitle("Dashboard Profesional | Reclaimers")
public class VistaPanelProfesional extends VerticalLayout {

    private Grid<SeguimientoProgreso> grid = new Grid<>(SeguimientoProgreso.class);

    public VistaPanelProfesional() {
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        add(new H1("Bienvenido al Dashboard de Profesional"));

        H2 subtitulo = new H2("Seguimientos de pacientes");
        subtitulo.getStyle().set("margin-top", "1rem");

        configurarGrid();
        cargarSeguimientos();

        add(subtitulo, grid);
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
        grid.setHeight("275px");
        grid.setWidthFull();              // Ancho completo
    }


    private void cargarSeguimientos() {
        try {
            String url = "http://localhost:8081/seguimiento/todos";
            RestTemplate restTemplate = new RestTemplate();
            SeguimientoProgreso[] datos = restTemplate.getForObject(url, SeguimientoProgreso[].class);
            grid.setItems(Arrays.asList(datos));

            // Añadimos el listener después de cargar los datos
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

    private String valorSeguro(Object valor) {
        return valor != null ? valor.toString() : "No especificado";
    }

}
