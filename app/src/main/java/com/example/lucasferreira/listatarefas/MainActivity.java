package com.example.lucasferreira.listatarefas;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Tarefa> listaTarefas;
    private ListView viewTarefas;
    private EditText editarNome;
    private Button botaoAdd;
    private Tarefa itemTarefa;
    private AdapterTarefas adaptador;
    private SQLiteDatabase bancoDadosTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botaoAdd = findViewById(R.id.botaoAdicionar);
        editarNome = findViewById(R.id.nomeTarefa);
        viewTarefas = findViewById(R.id.listaTarefas);
        listaTarefas = new ArrayList<>();

        //cria banco de dados
        bancoDadosTarefas = openOrCreateDatabase("kanban", MODE_PRIVATE, null);
        //cria tabela tarefas
        bancoDadosTarefas.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY, nome VARCHAR, descricao VARCHAR)");

        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (editarNome.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Digite o nome", Toast.LENGTH_SHORT).show();
                    } else {
                        itemTarefa = new Tarefa();
                        criarTarefa(itemTarefa);
                        recuperarListaDoIt();
                    }
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void recuperarListaDoIt() {
        adaptador = new AdapterTarefas(this, listaTarefas);
        viewTarefas.setAdapter(adaptador);
    }

    private void criarTarefa(Tarefa tarefa) {
        tarefa.setNome(editarNome.getText().toString());
        tarefa.setDescricao("Teste");
        listaTarefas.add(tarefa);
        bancoDadosTarefas.execSQL("INSERT INTO tarefas (id, nome, descricao) VALUES ('" + tarefa.getId()
                + "','" + tarefa.getNome() + "','" + tarefa.getDescricao() + "')");
        editarNome.setText("");
    }
    private void lerTarefa(){

    }
    private void removerTarefa(){

    }
}
