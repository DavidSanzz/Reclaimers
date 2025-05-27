package com.reclaimers.api_reclaimers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ServicioEmail {

    private final JavaMailSender mailSender;

    @Autowired
    public ServicioEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarPdfPorCorreo(String destinatario, byte[] pdfBytes, String nombreArchivo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destinatario);
        helper.setSubject("Informe de usuario - Reclaimers");
        helper.setText("Adjunto encontrarás el informe PDF con tu información.");

        // Adjuntar el PDF
        helper.addAttachment(nombreArchivo, new ByteArrayResource(pdfBytes));

        mailSender.send(mensaje);
    }
}
