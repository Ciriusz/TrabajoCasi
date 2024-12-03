package com.example.trabajo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabajo.datos.Repositorio;
import com.example.trabajo.model.Sensor;

import java.util.List;

public class RecyclerVerSensoresActivity extends AppCompatActivity {

    private ListView sensoresListView;
    private ArrayAdapter<Sensor> adapter; // Adaptador dinámico

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_ver_sensores);

        // Inicializar ListView
        sensoresListView = findViewById(R.id.sensoresListView);

        // Configurar adaptador con lista dinámica de sensores
        List<Sensor> sensores = Repositorio.getInstance().obtenerSensores();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensores);
        sensoresListView.setAdapter(adapter);

        // Vincular adaptador dinámico al repositorio
        configurarNotificacionCambios();
    }

    private void configurarNotificacionCambios() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        cargarSensores();
    }
    private void cargarSensores() {
        sensoresListView = findViewById(R.id.sensoresListView);
    }
}
