package com.example.leonardomoraes.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
    Adapter do recylcerView
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private ArrayList<Receita> receitaArrayList;
    private String recipeNome;
    private String recipeIngredientes;
    private String recipeTempo;
    private String recipePreparo;
    private String recipeTipo;
    private String recipeUri;
    private Context c;

    class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //ItemClickListener itemClickListener;
        TextView nome = (TextView) itemView.findViewById(R.id.tv_nomeReceita_Act_telaReceita);
        TextView tempo = (TextView) itemView.findViewById(R.id.tv_tempoReceita_Act_telaReceita);
        TextView tipo = (TextView) itemView.findViewById(R.id.tv_tipoReceita_Act_telaReceita);

        RecyclerHolder(View itemView) {
            super(itemView);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, OpenReceitaActivity.class);
            intent.putExtra("nome", recipeNome);
            intent.putExtra("ingredientes", recipeIngredientes);
            intent.putExtra("tempo", recipeTempo);
            intent.putExtra("preparo", recipePreparo);
            intent.putExtra("tipo", recipeTipo);
            intent.putExtra("uri", recipeUri);
            context.startActivity(intent);
        }
    }

    public RecyclerAdapter(ArrayList<Receita> receitaArrayList, Context c) {
        this.receitaArrayList = receitaArrayList;
        this.c = c;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.nome.setText(receitaArrayList.get(position).getNome());
        holder.tempo.setText(receitaArrayList.get(position).getTempo());
        holder.tipo.setText(receitaArrayList.get(position).getTipo());

        recipeNome = receitaArrayList.get(position).getNome();
        recipeIngredientes = receitaArrayList.get(position).getIngrediente();
        recipeTempo = receitaArrayList.get(position).getTempo();
        recipePreparo = receitaArrayList.get(position).getPreparo();
        recipeTipo = receitaArrayList.get(position).getTipo();
        recipeUri = receitaArrayList.get(position).getUrlFoto();

    }

    @Override
    public int getItemCount() {
        return receitaArrayList.size();
    }

}
