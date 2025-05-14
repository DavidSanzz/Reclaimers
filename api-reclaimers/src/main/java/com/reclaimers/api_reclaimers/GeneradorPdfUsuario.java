package com.reclaimers.api_reclaimers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.models.SeguimientoProgreso;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneradorPdfUsuario {

    public static byte[] generarPdf(Usuario usuario, List<SeguimientoProgreso> seguimientos) {
        try {
            Document documento = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(documento, baos);
            documento.open();

            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Informe del Usuario", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph("\n"));

            String tipoUsuarioLegible = usuario.getTipoUsuario() == Usuario.TipoUsuario.PROFESIONAL
                    ? "Profesional de apoyo"
                    : "Participante en recuperación";

            documento.add(new Paragraph("Nombre: " + usuario.getNombre()));
            documento.add(new Paragraph("Email: " + usuario.getEmail()));
            documento.add(new Paragraph("Tipo de usuario: " + tipoUsuarioLegible));
            if (usuario.getTipoJuegoFavorito() != null)
                documento.add(new Paragraph("Juego favorito: " + usuario.getTipoJuegoFavorito()));
            if (usuario.getHorarioFrecuenteJuego() != null)
                documento.add(new Paragraph("Horario frecuente: " + usuario.getHorarioFrecuenteJuego()));
            if (usuario.getTiempoSemanalJuego() != null)
                documento.add(new Paragraph("Tiempo semanal: " + usuario.getTiempoSemanalJuego() + " horas"));
            if (usuario.getDineroGastadoMensual() != null)
                documento.add(new Paragraph("Dinero mensual gastado: " + usuario.getDineroGastadoMensual() + " €"));
            if (usuario.getNivelAutoevaluacion() != null)
                documento.add(new Paragraph("Autoevaluación: " + usuario.getNivelAutoevaluacion()));
            if (usuario.getObjetivoPersonal() != null)
                documento.add(new Paragraph("Objetivo personal: " + usuario.getObjetivoPersonal()));

            documento.add(new Paragraph("\n"));

            Paragraph subtitulo = new Paragraph("Seguimientos", new Font(Font.HELVETICA, 14, Font.BOLD));
            documento.add(subtitulo);
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            tabla.addCell("Comentario");
            tabla.addCell("Progreso");
            tabla.addCell("Fecha");
            tabla.addCell("Nivel");
            tabla.addCell("Ánimo");
            tabla.addCell("Recaída");

            boolean sombrear = false;
            for (SeguimientoProgreso sp : seguimientos) {
                Color bg = sombrear ? new Color(240, 240, 240) : Color.WHITE;

                PdfPCell celda1 = new PdfPCell(new Phrase(sp.getComentario() != null ? sp.getComentario() : ""));
                celda1.setBackgroundColor(bg);
                tabla.addCell(celda1);

                PdfPCell celda2 = new PdfPCell(new Phrase(sp.getProgreso() != null ? sp.getProgreso() : ""));
                celda2.setBackgroundColor(bg);
                tabla.addCell(celda2);

                PdfPCell celda3 = new PdfPCell(new Phrase(sp.getFecha() != null ? sp.getFecha().toLocalDate().toString() : ""));
                celda3.setBackgroundColor(bg);
                tabla.addCell(celda3);

                PdfPCell celda4 = new PdfPCell(new Phrase(sp.getNivelProgreso() != null ? sp.getNivelProgreso().toString() : "-"));
                celda4.setBackgroundColor(bg);
                tabla.addCell(celda4);

                PdfPCell celda5 = new PdfPCell(new Phrase(sp.getEstadoAnimo() != null ? sp.getEstadoAnimo().toString() : "-"));
                celda5.setBackgroundColor(bg);
                tabla.addCell(celda5);

                PdfPCell celda6 = new PdfPCell(new Phrase(Boolean.TRUE.equals(sp.getRecaida()) ? "Sí" : "No"));
                celda6.setBackgroundColor(bg);
                tabla.addCell(celda6);

                sombrear = !sombrear;
            }
            documento.add(tabla);
            documento.add(new Paragraph("\n"));

            // Gráfico de evolución del nivel de progreso
            DefaultCategoryDataset datasetProgreso = new DefaultCategoryDataset();
            DateTimeFormatter formatoCorto = DateTimeFormatter.ofPattern("dd/MM");
            for (SeguimientoProgreso sp : seguimientos) {
                if (sp.getFecha() != null && sp.getNivelProgreso() != null) {
                    String fechaFormateada = sp.getFecha().toLocalDate().format(formatoCorto);
                    datasetProgreso.addValue(sp.getNivelProgreso(), "Nivel", fechaFormateada);
                }
            }
            JFreeChart chartProgreso = ChartFactory.createLineChart(
                    "Evolución del Nivel de Progreso",
                    "Fecha", "Nivel",
                    datasetProgreso,
                    PlotOrientation.VERTICAL,
                    false, true, false);

            BufferedImage chartImage1 = chartProgreso.createBufferedImage(500, 300);
            Image grafico1 = Image.getInstance(chartImage1, null);
            documento.add(grafico1);
            documento.add(new Paragraph("\n"));

            // Gráfico de distribución de estado de ánimo
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            Map<String, Long> estadoCounts = seguimientos.stream()
                    .filter(sp -> sp.getEstadoAnimo() != null)
                    .collect(Collectors.groupingBy(sp -> sp.getEstadoAnimo().toString(), Collectors.counting()));
            estadoCounts.forEach(pieDataset::setValue);

            JFreeChart chartEstado = ChartFactory.createPieChart(
                    "Distribución del Estado de Ánimo",
                    pieDataset,
                    true, true, false);
            BufferedImage chartImage2 = chartEstado.createBufferedImage(500, 300);
            Image grafico2 = Image.getInstance(chartImage2, null);
            documento.add(grafico2);
            documento.add(new Paragraph("\n"));

            // Gráfico de recaídas por fecha
            DefaultCategoryDataset datasetRecaidas = new DefaultCategoryDataset();
            Map<java.time.LocalDate, Long> recaidasPorFecha = seguimientos.stream()
                    .filter(sp -> sp.getFecha() != null && Boolean.TRUE.equals(sp.getRecaida()))
                    .collect(Collectors.groupingBy(sp -> sp.getFecha().toLocalDate(), Collectors.counting()));

            // Ordenar por fecha y luego formatear para el gráfico
            // reutiliza formatoCorto ya definido arriba
            recaidasPorFecha.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        String fechaFormateada = entry.getKey().format(formatoCorto);
                        datasetRecaidas.addValue(entry.getValue(), "Recaídas", fechaFormateada);
                    });

            JFreeChart chartRecaidas = ChartFactory.createBarChart(
                    "Recaídas por Fecha",
                    "Fecha", "Cantidad",
                    datasetRecaidas,
                    PlotOrientation.VERTICAL,
                    false, true, false);

            BufferedImage chartImage3 = chartRecaidas.createBufferedImage(500, 300);
            Image grafico3 = Image.getInstance(chartImage3, null);
            documento.add(grafico3);

            documento.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}