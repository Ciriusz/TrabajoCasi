package com.example.trabajo.datos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.trabajo.adapters.SensoresAdapters;
import com.example.trabajo.model.TipoSensor;
import com.example.trabajo.model.Ubicacion;
import com.example.trabajo.model.Sensor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repositorio {

    private static Repositorio instance = null;
    private List<TipoSensor> listaTipoSensor;
    private List<Ubicacion> listaUbicaciones;
    private List<Sensor> listaSensores;
    private SensoresAdapters adaptador; // Adaptador conectado

    // Constructor
    protected Repositorio() {
        listaTipoSensor = new ArrayList<>();
        listaUbicaciones = new ArrayList<>();
        listaSensores = new ArrayList<>();

        // Agregar tipos de sensor
        listaTipoSensor.add(new TipoSensor("Temperatura"));
        listaTipoSensor.add(new TipoSensor("Humedad"));

        // Ubicaciones de muestra
        listaUbicaciones.add(new Ubicacion("Invernadero", "Invernadero aislado"));
        listaUbicaciones.add(new Ubicacion("Asque", "Mediano de 100 personas"));
        listaUbicaciones.add(new Ubicacion("Sur", "Grande para 200 personas"));
    }

    // Singleton
    public static synchronized Repositorio getInstance() {
        if (instance == null) {
            instance = new Repositorio();
        }
        return instance;
    }

    // Configurar adaptador
    public void setAdaptador(SensoresAdapters adaptador) {
        this.adaptador = adaptador;
    }

    // Obtener tipos de sensor
    public List<String> obtenerTiposSensor() {
        List<String> tipos = new ArrayList<>();
        for (TipoSensor tipo : listaTipoSensor) {
            tipos.add(tipo.getNombre());
        }
        return tipos;
    }

    // Interfaz para el callback de ubicaciones
    public interface UbicacionesCallback {
        void onUbicacionesObtenidas(List<String> ubicaciones);
    }

    // Obtener ubicaciones para el spinner (usando el callback)
    public void obtenerUbicaciones(final UbicacionesCallback callback) {
        List<String> ubicaciones = new ArrayList<>();

        // Obtener ubicaciones de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ubicaciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        // Suponiendo que cada documento tiene un campo "nombre" para la ubicación
                        String nombre = document.getString("nombre");
                        if (nombre != null) {
                            ubicaciones.add(nombre);
                        }
                    }
                    // Llamamos al callback con la lista de ubicaciones obtenidas
                    callback.onUbicacionesObtenidas(ubicaciones);
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });

        // También agregamos ubicaciones locales
        for (Ubicacion ubicacion : listaUbicaciones) {
            ubicaciones.add(ubicacion.getNombre());
        }

        // Aquí no llamamos directamente al callback porque Firestore es asíncrono
    }

    // Agregar una ubicación
    public void agregarUbicacion(String nombre, String descripcion) {
        listaUbicaciones.add(new Ubicacion(nombre, descripcion));
    }

    // Agregar un sensor a la lista
    public void agregarSensor(Sensor sensor) {
        listaSensores.add(sensor);
        if (adaptador != null) adaptador.actualizarLista(listaSensores); // Notificación al adaptador
    }

    // Eliminar un sensor de la lista
    public void eliminarSensor(Sensor sensor) {
        int index = listaSensores.indexOf(sensor);
        if (index >= 0) {
            listaSensores.remove(index);
            if (adaptador != null) {
                adaptador.actualizarLista(listaSensores); // Notificar al adaptador
            }
        }
    }

    // Actualizar un sensor en la lista
    public void actualizarSensor(Sensor sensor) {
        for (int i = 0; i < listaSensores.size(); i++) {
            if (listaSensores.get(i).getNombre().equals(sensor.getNombre())) {
                listaSensores.set(i, sensor);
                if (adaptador != null) adaptador.notifyItemChanged(i);
                break;
            }
        }
    }

    // Obtener lista de sensores para el adaptador
    public List<Sensor> obtenerSensores() {
        return new ArrayList<>(listaSensores);
    }
}
