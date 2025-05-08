package com.example.univalle_map;

public class Evento {
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String lugar;

    public Evento(String titulo, String descripcion, String fecha, String hora,String lugar) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getLugar() {
        return lugar;
    }
}

