package com.example.lucasferreira.listatarefas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Tarefa> listaTarefas;
    private ListView viewTarefas;
    private EditText editarNome;
    private Button botaoAdd;
    private Tarefa itemTarefa;
    private AdapterTarefas adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botaoAdd = findViewById(R.id.botaoAdicionar);
        editarNome = findViewById(R.id.nomeTarefa);
        viewTarefas = findViewById(R.id.listaTarefas);

        listaTarefas =  new ArrayList<>();

        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTarefa = new Tarefa();
                itemTarefa.setNome(editarNome.getText().toString());
                itemTarefa.setDescricao("Teste");
                editarNome.setText("");
                listaTarefas.add(itemTarefa);
                recuperarLista();
            }
        });
    }
    private void recuperarLista(){

        adaptador = new AdapterTarefas(this,listaTarefas);
        viewTarefas.setAdapter(adaptador);
    }
}
