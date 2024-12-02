package com.example.trabajo.model;

public class TipoSensor {
    private String nombre;

    // Constructor vacío requerido por Firebase
    public TipoSensor() {}

    // Constructor con parámetros
    public TipoSensor(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método toString para representación en texto
    @Override
    public String toString() {
        return nombre;
    }
}