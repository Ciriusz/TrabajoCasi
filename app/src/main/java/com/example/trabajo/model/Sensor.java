package com.example.trabajo.model;

import java.util.HashMap;
import java.util.Map;

public class Sensor {
    private String id; // Nuevo campo para almacenar el ID del documento en Firebase
    private String nombre;
    private String descripcion;
    private float ideal;
    private String tipoSensor;
    private String ubicacion;

    // Constructor vacío necesario para Firestore
    public Sensor() {
    }

    // Constructor original
    public Sensor(String nombre, String descripcion, float ideal, String tipoSensor, String ubicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ideal = ideal;
        this.tipoSensor = tipoSensor;
        this.ubicacion = ubicacion;
    }

    // Constructor adicional que incluye ID
    public Sensor(String id, String nombre, String descripcion, float ideal, String tipoSensor, String ubicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ideal = ideal;
        this.tipoSensor = tipoSensor;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public float getIdeal() {
        return ideal;
    }

    public void setIdeal(float ideal) {
        this.ideal = ideal;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // Método para convertir a un mapa (requerido por Firestore)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("descripcion", descripcion);
        map.put("ideal", ideal);
        map.put("tipoSensor", tipoSensor);
        map.put("ubicacion", ubicacion);
        return map;
    }

    // Método para convertir desde un mapa (útil para Firestore)
    public static Sensor fromMap(String id, Map<String, Object> map) {
        String nombre = (String) map.get("nombre");
        String descripcion = (String) map.get("descripcion");
        float ideal = map.get("ideal") instanceof Double ? ((Double) map.get("ideal")).floatValue() : (float) map.get("ideal");
        String tipoSensor = (String) map.get("tipoSensor");
        String ubicacion = (String) map.get("ubicacion");
        return new Sensor(id, nombre, descripcion, ideal, tipoSensor, ubicacion);
    }

    @Override
    public String toString() {
        return "Nombre del sensor: " + nombre + "\nDescripción: " + descripcion + "\nIdeal: " + ideal +
                "\nTipo: " + tipoSensor + "\nUbicación: " + ubicacion;
    }
}