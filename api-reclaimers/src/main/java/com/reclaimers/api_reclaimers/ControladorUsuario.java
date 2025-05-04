package com.reclaimers.api_reclaimers;

import com.reclaimers.api_reclaimers.models.Usuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioUsuario;
import com.reclaimers.api_reclaimers.repositorios.RepositorioSeguimientoProgreso;
import com.reclaimers.api_reclaimers.models.SeguimientoProgreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuario {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSeguimientoProgreso repositorioSeguimientoProgreso;

    @Autowired
    public ControladorUsuario(RepositorioUsuario repositorioUsuario,
                              RepositorioSeguimientoProgreso repositorioSeguimientoProgreso) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioSeguimientoProgreso = repositorioSeguimientoProgreso;
    }

    @Autowired
    private ServicioEmail servicioEmail;

    // Obtener todos los usuarios (uso general)
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return repositorioUsuario.findAll();
    }

    // Login: devuelve usuario completo si las credenciales son válidas
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuario, HttpSession session) {
        Usuario usuarioExistente = repositorioUsuario.findByEmail(usuario.getEmail());

        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean coincide = Encriptador.verificar(usuario.getContrasena(), usuarioExistente.getContrasena());

        if (!coincide) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        session.setAttribute("usuarioId", usuarioExistente.getId());
        return ResponseEntity.ok(usuarioExistente);
    }

    // Obtener el usuario actualmente logueado
    @GetMapping("/usuarioActual")
    public ResponseEntity<Usuario> getUsuarioActual(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Usuario usuario = repositorioUsuario.findById(usuarioId).orElse(null);
        return ResponseEntity.ok(usuario);
    }

    // Registro de nuevo usuario
    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        Map<String, String> errores = new HashMap<>();

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }

        if (usuario.getEmail() == null || !usuario.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errores.put("email", "El email no es válido");
        }

        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 6) {
            errores.put("contrasena", "La contraseña debe tener al menos 6 caracteres");
        }

        if (repositorioUsuario.findByEmail(usuario.getEmail()) != null) {
            errores.put("email", "El correo ya está registrado");
        }

        if (!errores.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (Map.Entry<String, String> entry : errores.entrySet()) {
                errorMessages.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            return ResponseEntity.status(HttpStatus.OK).body(errorMessages.toString());
        }

        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.LUDOPATA);
        }

        usuario.setContrasena(Encriptador.encriptar(usuario.getContrasena()));
        repositorioUsuario.save(usuario);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Usuario registrado correctamente como " + usuario.getTipoUsuario());
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        if (!repositorioUsuario.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        repositorioUsuario.deleteById(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    // Actualizar los nuevos campos adicionales del usuario ludópata
    @PutMapping("/{id}/informacion-adicional")
    public ResponseEntity<String> actualizarInformacionAdicional(
            @PathVariable Long id,
            @RequestBody Usuario datosActualizados) {

        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Solo actualizar si no son null
        if (datosActualizados.getTipoJuegoFavorito() != null) {
            usuario.setTipoJuegoFavorito(datosActualizados.getTipoJuegoFavorito());
        }
        if (datosActualizados.getHorarioFrecuenteJuego() != null) {
            usuario.setHorarioFrecuenteJuego(datosActualizados.getHorarioFrecuenteJuego());
        }
        if (datosActualizados.getTiempoSemanalJuego() != null) {
            usuario.setTiempoSemanalJuego(datosActualizados.getTiempoSemanalJuego());
        }
        if (datosActualizados.getDineroGastadoMensual() != null) {
            usuario.setDineroGastadoMensual(datosActualizados.getDineroGastadoMensual());
        }
        if (datosActualizados.getNivelAutoevaluacion() != null) {
            usuario.setNivelAutoevaluacion(datosActualizados.getNivelAutoevaluacion());
        }
        if (datosActualizados.getObjetivoPersonal() != null) {
            usuario.setObjetivoPersonal(datosActualizados.getObjetivoPersonal());
        }

        repositorioUsuario.save(usuario);

        return ResponseEntity.ok("Información adicional del usuario actualizada correctamente");
    }

    @GetMapping("/{id}/generar-pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = usuarioOpt.get();

        // Obtener seguimientos desde el repositorio, no desde el usuario
        List<SeguimientoProgreso> seguimientos = repositorioSeguimientoProgreso.findByUsuarioId(id);

        try {
            byte[] pdfBytes = GeneradorPdfUsuario.generarPdf(usuario, seguimientos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuario_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/enviar-pdf")
    public ResponseEntity<String> enviarPdfPorCorreo(
            @PathVariable Long id,
            @RequestParam String correoDestino) {

        Optional<Usuario> usuarioOpt = repositorioUsuario.findById(id);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        List<SeguimientoProgreso> seguimientos = repositorioSeguimientoProgreso.findByUsuarioId(id);

        try {
            // Decodificar el correo si viene con caracteres codificados como %40
            String correoDecodificado = URLDecoder.decode(correoDestino, StandardCharsets.UTF_8);

            byte[] pdf = GeneradorPdfUsuario.generarPdf(usuario, seguimientos);
            servicioEmail.enviarPdfPorCorreo(correoDecodificado, pdf, "usuario_" + id + ".pdf");

            return ResponseEntity.ok("PDF enviado correctamente a " + correoDecodificado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el PDF");
        }
    }

}
