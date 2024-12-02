package com.example.trabajo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabajo.datos.Repositorio;
import com.example.trabajo.model.Sensor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private EditText nombreEditText, descripcionEditText, idealEditText;
    private Button ingresarSensorButton, verSensoresButton, modificarSensorButton, eliminarSensorButton, buscarSensorButton;
    private Spinner spinnerTipoSensor, spinnerUbicacion;
    private Sensor sensorSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        inicializarComponentes();
        configurarSpinners();
        configurarBotones();
    }

    private void inicializarComponentes() {
        nombreEditText = findViewById(R.id.sensores);
        descripcionEditText = findViewById(R.id.modeloSensorEditText);
        idealEditText = findViewById(R.id.idealEditText);
        ingresarSensorButton = findViewById(R.id.ingresarSensorButton);
        verSensoresButton = findViewById(R.id.SensoresHistorial);
        modificarSensorButton = findViewById(R.id.ModificarSensorButton);
        eliminarSensorButton = findViewById(R.id.eliminarSensorButton);
        buscarSensorButton = findViewById(R.id.buscarSensorButton);
        spinnerTipoSensor = findViewById(R.id.tipoSensorSpinner);
        spinnerUbicacion = findViewById(R.id.ubicacionSpinner);
    }

    private void configurarSpinners() {
        // Obtener los tipos de sensores de manera sincrónica
        List<String> tiposSensor = Repositorio.getInstance().obtenerTiposSensor();

        // Configurar el Spinner para tipos de sensor
        ArrayAdapter<String> tipoSensorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposSensor);
        tipoSensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(tipoSensorAdapter);

        // Obtener las ubicaciones de manera asíncrona con un callback
        Repositorio.getInstance().obtenerUbicaciones(new Repositorio.UbicacionesCallback() {
            @Override
            public void onUbicacionesObtenidas(List<String> ubicaciones) {
                // Actualizar el Spinner de ubicaciones con la lista obtenida
                ArrayAdapter<String> ubicacionAdapter = new ArrayAdapter<>(SensorActivity.this, android.R.layout.simple_spinner_item, ubicaciones);
                ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUbicacion.setAdapter(ubicacionAdapter);
            }
        });
    }

    private void configurarBotones() {
        ingresarSensorButton.setOnClickListener(v -> validarYGuardarDatosEnFirebase());
        verSensoresButton.setOnClickListener(v -> startActivity(new Intent(SensorActivity.this, RecyclerVerSensoresActivity.class)));
        buscarSensorButton.setOnClickListener(v -> buscarSensor());
        modificarSensorButton.setOnClickListener(v -> modificarSensor());
        eliminarSensorButton.setOnClickListener(v -> eliminarSensor());
    }

    private boolean validarNombre(String nombre) {
        if (TextUtils.isEmpty(nombre)) {
            mostrarToast("El nombre es obligatorio");
            return false;
        }
        if (nombre.length() < 5 || nombre.length() > 15) {
            mostrarToast("El nombre debe tener entre 5 y 15 caracteres");
            return false;
        }
        return true;
    }

    private boolean validarDescripcion(String descripcion) {
        if (!TextUtils.isEmpty(descripcion) && descripcion.length() > 30) {
            mostrarToast("La descripción no debe exceder los 30 caracteres");
            return false;
        }
        return true;
    }

    private boolean validarIdeal(String idealStr) {
        if (TextUtils.isEmpty(idealStr)) {
            mostrarToast("El valor ideal es obligatorio");
            return false;
        }
        try {
            float ideal = Float.parseFloat(idealStr);
            if (ideal <= 0) {
                mostrarToast("El valor ideal debe ser un número positivo");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarToast("El valor ideal debe ser un número válido");
            return false;
        }
        return true;
    }

    private void validarYGuardarDatosEnFirebase() {
        String nombre = nombreEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String idealStr = idealEditText.getText().toString().trim();

        if (!validarNombre(nombre) || !validarDescripcion(descripcion) || !validarIdeal(idealStr)) {
            return;
        }

        float ideal = Float.parseFloat(idealStr);
        String tipoSensor = spinnerTipoSensor.getSelectedItem().toString();
        String ubicacion = spinnerUbicacion.getSelectedItem().toString();

        Sensor sensor = new Sensor(nombre, descripcion, ideal, tipoSensor, ubicacion);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sensores").document()
                .set(sensor.toMap())
                .addOnSuccessListener(unused -> mostrarToast("Sensor agregado correctamente"))
                .addOnFailureListener(e -> mostrarToast("Error al guardar el sensor: " + e.getMessage()));

        Repositorio.getInstance().agregarSensor(sensor);
    }

    private void buscarSensor() {
        String nombre = nombreEditText.getText().toString().trim();

        if (!validarNombre(nombre)) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sensores")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        mostrarToast("Sensor no encontrado");
                    } else {
                        sensorSeleccionado = queryDocumentSnapshots.getDocuments().get(0).toObject(Sensor.class);
                        cargarDatosSensorEnFormulario();
                    }
                })
                .addOnFailureListener(e -> mostrarToast("Error al buscar el sensor: " + e.getMessage()));
    }

    private void cargarDatosSensorEnFormulario() {
        if (sensorSeleccionado != null) {
            nombreEditText.setText(sensorSeleccionado.getNombre());
            descripcionEditText.setText(sensorSeleccionado.getDescripcion());
            idealEditText.setText(String.valueOf(sensorSeleccionado.getIdeal()));

            spinnerTipoSensor.setSelection(obtenerPosicionSpinner(spinnerTipoSensor, sensorSeleccionado.getTipoSensor()));
            spinnerUbicacion.setSelection(obtenerPosicionSpinner(spinnerUbicacion, sensorSeleccionado.getUbicacion()));
        }
    }

    private void modificarSensor() {
        if (sensorSeleccionado == null) {
            mostrarToast("No se ha seleccionado un sensor para modificar");
            return;
        }

        String nombre = nombreEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String idealStr = idealEditText.getText().toString().trim();

        if (!validarNombre(nombre) || !validarDescripcion(descripcion) || !validarIdeal(idealStr)) {
            return;
        }

        actualizarSensorSeleccionado(nombre, descripcion, idealStr);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sensores")
                .whereEqualTo("nombre", sensorSeleccionado.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("sensores").document(documentId)
                                .set(sensorSeleccionado.toMap())
                                .addOnSuccessListener(aVoid -> mostrarToast("Sensor modificado correctamente"))
                                .addOnFailureListener(e -> mostrarToast("Error al modificar el sensor: " + e.getMessage()));
                    } else {
                        mostrarToast("El sensor no existe en la base de datos.");
                    }
                })
                .addOnFailureListener(e -> mostrarToast("Error al realizar la consulta: " + e.getMessage()));
    }

    private void eliminarSensor() {
        if (sensorSeleccionado == null) {
            mostrarToast("No se ha seleccionado un sensor para eliminar");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sensores")
                .whereEqualTo("nombre", sensorSeleccionado.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("sensores").document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> mostrarToast("Sensor eliminado correctamente"))
                                .addOnFailureListener(e -> mostrarToast("Error al eliminar el sensor: " + e.getMessage()));
                    } else {
                        mostrarToast("El sensor no existe en la base de datos.");
                    }
                })
                .addOnFailureListener(e -> mostrarToast("Error al realizar la consulta: " + e.getMessage()));
    }

    private void actualizarSensorSeleccionado(String nombre, String descripcion, String idealStr) {
        sensorSeleccionado.setNombre(nombre);
        sensorSeleccionado.setDescripcion(descripcion);
        sensorSeleccionado.setIdeal(Float.parseFloat(idealStr));
        sensorSeleccionado.setTipoSensor(spinnerTipoSensor.getSelectedItem().toString());
        sensorSeleccionado.setUbicacion(spinnerUbicacion.getSelectedItem().toString());
    }

    private int obtenerPosicionSpinner(Spinner spinner, String valor) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return adapter.getPosition(valor);
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

