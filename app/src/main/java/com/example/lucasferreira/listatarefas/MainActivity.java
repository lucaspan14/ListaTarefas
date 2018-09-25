package com.example.lucasferreira.listatarefas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdd;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDeDados;
    private AdapterTarefas adaptadorTarefa;
    private List<Tarefa> itens;
    private Tarefa tarefaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            // recuperar componentes
            textoTarefa = findViewById(R.id.nomeTarefa);
            botaoAdd = findViewById(R.id.botaoAdicionar);
            //banco de dados
            bancoDeDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);
            bancoDeDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");
            botaoAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefa(textoDigitado);
                }

            });
            // Listar tarefas
            recuperarTarefas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarTarefa(String texto) {
        try {
            if (texto.equals("")) {
                Toast.makeText(MainActivity.this, "Digite uma tarefa ", Toast.LENGTH_SHORT).show();
            } else {
                bancoDeDados.execSQL("INSERT INTO tarefas (tarefa) VALUES ('" + texto + "')");
                recuperarTarefas();
                textoTarefa.setText("");
                tarefaLista = new Tarefa(texto, "descrição");
                itens.add(tarefaLista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void recuperarTarefas() {
        try {
            //recuperar as tarefas
            Cursor cursor = bancoDeDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);
            //recuperar o indice
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");
            listaTarefas();
            cursor.moveToFirst();
            while (cursor != null) {
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listaTarefas() {
        try {
            listaTarefas = findViewById(R.id.listaTarefas);
            adaptadorTarefa = new AdapterTarefas(this, itens);
            listaTarefas.setAdapter(adaptadorTarefa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
