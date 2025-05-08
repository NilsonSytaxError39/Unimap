package com.example.univalle_map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> listaEventos;

    public EventoAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new EventoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);
        holder.titulo.setText(evento.getTitulo());
        holder.fecha.setText(evento.getFecha());
        holder.descripcion.setText(evento.getDescripcion());
        holder.hora.setText(evento.getHora());
        holder.lugar.setText((evento.getLugar()));
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion, fecha, hora, lugar;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTituloEvento);
            fecha = itemView.findViewById(R.id.txtFechaEvento);
            descripcion = itemView.findViewById(R.id.txtDescripcionEvento);
            hora = itemView.findViewById(R.id.txtHoraEvento);
            lugar = itemView.findViewById(R.id.txtLugarEvento);

        }
    }
}

