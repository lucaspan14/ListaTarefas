package com.example.lucasferreira.listatarefas;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Tarefa> listaTarefas;
    private ListView viewTarefas;
    private EditText editarNome;
    private Tarefa itemTarefa;
    private SQLiteDatabase bancoDadosTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //localizar na activity.
        Button botaoAdd = findViewById(R.id.botaoAdicionar);
        editarNome = findViewById(R.id.nomeTarefa);
        viewTarefas = findViewById(R.id.listaTarefas);
        listaTarefas = new ArrayList<>();
        //cria banco de dados
        bancoDadosTarefas = openOrCreateDatabase("kanban", MODE_PRIVATE, null);
        try {
            recuperarListaDoIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //seta click para os itens
        viewTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView texto = view.findViewById(R.id.nome);
                TextView descricao = view.findViewById(R.id.descricao);
                TextView idTarefaClicado = view.findViewById(R.id.idTarefa);
                String msg = texto.getText().toString() + " Descrição: " + descricao.getText().toString() + idTarefaClicado.getText().toString();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Integer pegaId =  Integer.parseInt(idTarefaClicado.getText().toString());
                removerTarefa(pegaId);
                return false;
            }
        });

        //cria tabela tarefas
        bancoDadosTarefas.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, descricao VARCHAR)");
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
        //limpa a lista de tarefa
        listaTarefas.clear();
        //executa query
        Cursor cursor = bancoDadosTarefas.rawQuery("SELECT * FROM tarefas ORDER BY id DESC ", null);
        cursor.moveToFirst();
        //criar o adaptador;
        AdapterTarefas adaptador;
        //instancia o adaptador
        adaptador = new AdapterTarefas(this, listaTarefas);
        //seta o adaptador;
        viewTarefas.setAdapter(adaptador);
        while (cursor != null) {
            Tarefa lerTarefa = new Tarefa();
            lerTarefa.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            lerTarefa.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            lerTarefa.setId(cursor.getInt(cursor.getColumnIndex("id")));
            listaTarefas.add(lerTarefa);
            cursor.moveToNext();
            Log.i("resultado - ", String.valueOf(lerTarefa.getId()));
            Log.i("nome - ", cursor.getString(cursor.getColumnIndex("nome")));
        }
        cursor.close();
    }

    private void criarTarefa(Tarefa tarefa) {
        tarefa.setNome(editarNome.getText().toString());
        tarefa.setDescricao("novos testes");
        bancoDadosTarefas.execSQL("INSERT INTO tarefas (nome, descricao) VALUES ('" + tarefa.getNome() + "','" + tarefa.getDescricao() + "')");
        editarNome.setText("");
    }

    private void removerTarefa(Integer id) {
        try {
            bancoDadosTarefas.execSQL("DELETE FROM tarefas WHERE id=" + id.toString());
            recuperarListaDoIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
