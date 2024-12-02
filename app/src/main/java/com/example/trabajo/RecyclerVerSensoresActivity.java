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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_ver_sensores);

        // Inicializar ListView
        sensoresListView = findViewById(R.id.sensoresListView); // Ahora usa el ID correcto del ListView

        // Obtener la lista de sensores desde el Repositorio
        List<Sensor> sensores = Repositorio.getInstance().obtenerSensores();

        // Crear un adaptador para mostrar los sensores en la ListView
        ArrayAdapter<Sensor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensores);
        sensoresListView.setAdapter(adapter);
    }
}
