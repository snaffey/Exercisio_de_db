package com.example.db.db;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.R;
import com.example.db.db.model.Artigo;

import java.util.List;

public class ArtigoAdapter extends RecyclerView.Adapter<ArtigoAdapter.ArtigoViewHolder>{
    List<Artigo> artigos;
    Context ctx;
    ArtigoAdapter(List<Artigo> artigos, Context ctx){
        this.artigos = artigos;
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public ArtigoAdapter.ArtigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bditem, parent, false);
        ArtigoViewHolder avh = new ArtigoViewHolder(v, this.ctx);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtigoAdapter.ArtigoViewHolder holder, int position) {
        holder.id.setText(String.valueOf(artigos.get(position).getId()));
        holder.titulo.setText(artigos.get(position).getTitulo());
        holder.url.setText(artigos.get(position).getUrl());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ArtigoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id;
        TextView titulo;
        TextView url;
        Context ctx;
        public ArtigoViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            id = itemView.findViewById(R.id.idView);
            titulo = itemView.findViewById(R.id.tituloView);
            url = itemView.findViewById(R.id.urlView);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: " + titulo.getText().toString());
        }
    }
}