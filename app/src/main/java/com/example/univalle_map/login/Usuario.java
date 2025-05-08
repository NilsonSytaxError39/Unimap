package com.example.univalle_map.login;

public class Usuario {
    private String nombre;
    private String email;
    private String fechaRegistro;
    private String fotoUrl;

    // Constructor vacío requerido para Firebase
    public Usuario() {
    }

    public Usuario(String nombre, String email, String fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
        this.fotoUrl = null;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
} 