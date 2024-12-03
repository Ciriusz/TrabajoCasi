package com.example.trabajo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajo.R;
import com.example.trabajo.model.Sensor;

import java.util.List;

public class SensoresAdapters extends RecyclerView.Adapter<SensoresAdapters.ViewHolder> {

    private List<Sensor> sensores;

    public SensoresAdapters(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycler_ver_sensores, parent, false);  // Asegúrate de que este layout exista
        return new ViewHolder(view);
    }

    // Actualiza la lista de sensores y notifica al adaptador
    public void actualizarLista(List<Sensor> nuevosSensores) {
        this.sensores = nuevosSensores;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sensor sensor = sensores.get(position);
        holder.getTextViewNombre().setText(sensor.getNombre());
        holder.getTextViewDescripcion().setText(sensor.getDescripcion());
        holder.getTextViewIdeal().setText(String.valueOf(sensor.getIdeal()));
    }

    @Override
    public int getItemCount() {
        return sensores.size();
    }

    // ViewHolder para la representación del sensor
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombre;
        private TextView textViewDescripcion;
        private TextView textViewIdeal;

        public ViewHolder(View view) {
            super(view);
            textViewNombre = view.findViewById(R.id.sensores); // Ajusta los IDs si es necesario
            textViewDescripcion = view.findViewById(R.id.modeloSensorEditText);
            textViewIdeal = view.findViewById(R.id.idealEditText);
        }

        public TextView getTextViewNombre() { return textViewNombre; }
        public TextView getTextViewDescripcion() { return textViewDescripcion; }
        public TextView getTextViewIdeal() { return textViewIdeal; }
    }
}
