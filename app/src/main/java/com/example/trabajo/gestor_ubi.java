package com.example.trabajo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class gestor_ubi extends AppCompatActivity {

    private EditText ubiSensorEditText;
    private EditText descripcionEditText;
    private Button ingresarUbiButton;
    private Button ubisIngresadasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_ubi);

        // Inicializar vistas
        ubiSensorEditText = findViewById(R.id.UbiSensor2);
        descripcionEditText = findViewById(R.id.descripc);
        ingresarUbiButton = findViewById(R.id.ingresarUbiButton);
        ubisIngresadasButton = findViewById(R.id.ubisIngresadas);

        // Configurar el botón para ingresar ubicación
        ingresarUbiButton.setOnClickListener(v -> validarDatos());

        // Configurar el botón para ver ubicaciones ingresadas
        ubisIngresadasButton.setOnClickListener(v -> verUbicacionesIngresadas());
    }

    // Validación de datos
    private void validarDatos() {
        String nombre = ubiSensorEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "¡El nombre es OBLIGATORIO!", Toast.LENGTH_SHORT).show();
            return;
        } else if (nombre.length() < 5 || nombre.length() > 15) {
            Toast.makeText(this, "El nombre debe tener entre 5 y 15 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(descripcion) && descripcion.length() > 30) {
            Toast.makeText(this, "La descripción no debe ser tan larga (max 30 caractéres)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar ubicación en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa con los datos
        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("nombre", nombre);
        ubicacion.put("descripcion", descripcion);

        // Agregar la ubicación a la colección "ubicaciones" en Firestore
        db.collection("ubicaciones")
                .add(ubicacion)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Ubicación agregada correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad después de guardar la ubicación
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al agregar la ubicación", Toast.LENGTH_SHORT).show();
                });
    }

    private void verUbicacionesIngresadas() {
        // Asegúrate de que la clase que vas a abrir sea la correcta para ver las ubicaciones
        Intent intent = new Intent(gestor_ubi.this, listarUbicaciones.class);
        startActivity(intent); // Abre la actividad de ver ubicaciones
    }
}
