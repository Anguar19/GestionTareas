/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 *
 * @author Anguar Alberto Rodriguez Fonseca
 */
public class Tarea {
    private Long id;
    private String titulo;
    private int prioridad; // 1=Alta, 2=Media, 3=Baja
    private boolean estado; // true=Hecho, false=Pendiente
    private boolean especial; // Marca especial ★
    private LocalDate fechaCreacion;
    private boolean activo;
    private LocalDateTime fechaModificacion;
    private String usuarioModificacion;
    
     public Tarea() {
        this.fechaCreacion = LocalDate.now();
        this.fechaModificacion = LocalDateTime.now();
        this.activo = true;
        this.estado = false;
        this.especial = false;
        this.usuarioModificacion = "SYSTEM";
    }

    public Tarea(String titulo, int prioridad) {
        this();
        this.titulo = titulo;
        this.prioridad = prioridad;
        validar();
    }

    // Método de validación
    private void validar() {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TaskManagerException("El título no puede estar vacío");
        }
        if (prioridad < 1 || prioridad > 3) {
            throw new TaskManagerException("La prioridad debe estar entre 1 y 3");
        }
    }
    
     public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TaskManagerException("El título no puede estar vacío");
        }
        this.titulo = titulo.trim();
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) {
            throw new TaskManagerException("La prioridad debe estar entre 1 y 3");
        }
        this.prioridad = prioridad;
    }

    public String getPrioridadTexto() {
        switch (prioridad) {
            case 1: return "Alta";
            case 2: return "Media";
            case 3: return "Baja";
            default: return "Desconocida";
        }
    }

    public String getEstadoTexto() {
        return estado ? "Hecho" : "Pendiente";
    }

    public String getMarcaEspecial() {
        return especial ? "★" : "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEspecial() {
        return especial;
    }

    public void setEspecial(boolean especial) {
        this.especial = especial;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }
    
    @Override
    public String toString() {
        return String.format("Tarea[%d]: %s (Prioridad: %s, Estado: %s) %s", 
                            id, titulo, getPrioridadTexto(), getEstadoTexto(), getMarcaEspecial());
    }

    public int getPrioridad() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getTitulo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
