package com.reclaimers.api_reclaimers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.models.SeguimientoProgreso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class GeneradorPdfUsuario {

    public static byte[] generarPdf(Usuario usuario, List<SeguimientoProgreso> seguimientos) {
        try {
            Document documento = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(documento, baos);
            documento.open();

            // Título principal
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Informe del Usuario", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("\n"));

            // Descripción respetuosa del tipo de usuario
            String tipoUsuarioLegible = usuario.getTipoUsuario() == Usuario.TipoUsuario.PROFESIONAL
                    ? "Profesional de apoyo"
                    : "Participante en recuperación";

            // Información general del usuario
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

            // Tabla de seguimientos
            Paragraph subtitulo = new Paragraph("Seguimientos", new Font(Font.HELVETICA, 14, Font.BOLD));
            documento.add(subtitulo);
            documento.add(new Paragraph("\n"));

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);
            tabla.addCell("Comentario");
            tabla.addCell("Progreso");

            for (SeguimientoProgreso sp : seguimientos) {
                tabla.addCell(sp.getComentario());
                tabla.addCell(sp.getProgreso());
            }

            documento.add(tabla);
            documento.close();

            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
