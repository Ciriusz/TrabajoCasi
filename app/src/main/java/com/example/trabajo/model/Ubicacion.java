package com.example.trabajo.model;

public class Ubicacion {
    private String nombre;
    private String descripcion;

    // Constructor vacío necesario para Firestore
    public Ubicacion() {}

    // Constructor con parámetros
    public Ubicacion(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
