package org.vaadin.example.models;

public class Mensaje {

    private Long id;
    private Long emisorId;
    private Long receptorId;
    private String contenido;
    private String fechaEnvio; // Cambio aquí: de 'timestamp' a 'fechaEnvio'

    public Mensaje() {
    }

    public Mensaje(Long emisorId, Long receptorId, String contenido) {
        this.emisorId = emisorId;
        this.receptorId = receptorId;
        this.contenido = contenido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
