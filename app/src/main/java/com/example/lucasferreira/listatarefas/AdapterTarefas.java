package com.example.lucasferreira.listatarefas;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterTarefas extends BaseAdapter {
    private List<Tarefa> tarefas;
    private Activity activity;
    public AdapterTarefas(Activity activity, List<Tarefa> tarefas) {
        this.activity = activity;
        this.tarefas = tarefas;
    }
    @Override
    public int getCount() {
        return tarefas.size();
    }
    @Override
    public Object getItem(int position) {
        return tarefas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.tarefalayout,parent, false);
        Tarefa tarefa = tarefas.get(position);
        TextView nome =  view.findViewById(R.id.nome);
        nome.setText(tarefa.getNome());
        TextView descricao = view.findViewById(R.id.descricao);
        descricao.setText(tarefa.getDescricao());
        return view;
    }
}
