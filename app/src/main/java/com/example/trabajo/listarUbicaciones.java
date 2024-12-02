package com.example.trabajo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajo.adapters.UbicacionesAdapter;
import com.example.trabajo.model.Ubicacion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class listarUbicaciones extends AppCompatActivity {

    private RecyclerView recyclerViewUbicaciones;
    private UbicacionesAdapter ubicacionesAdapter;
    private List<Ubicacion> ubicacionesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ubicaciones);

        // Inicialización del RecyclerView
        recyclerViewUbicaciones = findViewById(R.id.recyclerViewUbicaciones);
        recyclerViewUbicaciones.setLayoutManager(new LinearLayoutManager(this)); // Usamos un LinearLayoutManager

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Crear la lista de ubicaciones
        ubicacionesList = new ArrayList<>();

        // Consultar Firestore para obtener las ubicaciones
        obtenerUbicacionesDeFirebase();
    }

    private void obtenerUbicacionesDeFirebase() {
        try {
            db.collection("ubicaciones") // Nombre de la colección
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Convertir los documentos de Firestore a objetos Ubicacion
                            List<Ubicacion> ubicaciones = queryDocumentSnapshots.toObjects(Ubicacion.class);
                            ubicacionesList.clear();
                            ubicacionesList.addAll(ubicaciones);

                            // Configurar el adaptador para el RecyclerView
                            ubicacionesAdapter = new UbicacionesAdapter(ubicacionesList);
                            recyclerViewUbicaciones.setAdapter(ubicacionesAdapter);
                        } else {
                            Toast.makeText(this, "No hay ubicaciones en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al obtener las ubicaciones: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
