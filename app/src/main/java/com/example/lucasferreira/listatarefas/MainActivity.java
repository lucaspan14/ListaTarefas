package com.example.lucasferreira.listatarefas;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import android.view.WindowManager;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private List<Tarefa> listaTarefas;
    private ListView viewTarefas;
    private EditText addNomeTarefa;
    private EditText addDescricaoTarefa;
    private Tarefa itemTarefa;
    private SQLiteDatabase bancoDadosTarefas;
    private LinearLayout menuBotao;
    private View addTarefaLayout;
    private Button botaoAddTarefa;
    private Button botaoVoltarAdd;
    private boolean flag = true;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //esconde barra de menu.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //esconde Title bar
        try {
            getSupportActionBar().hide();
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);

        //Criar botao Menu
        menuBotao =  findViewById(R.id.layoutMenu_id);

        final ViewGroup.LayoutParams params = menuBotao.getLayoutParams();
        //localiza botao de cancelar o add tarefa
        addTarefaLayout = findViewById(R.id.addTarefaLayout);
        botaoVoltarAdd = findViewById(R.id.cancelarAddTarefa_id);
        botaoVoltarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTarefaLayout.getVisibility() == addTarefaLayout.VISIBLE){
                    addTarefaLayout.setVisibility(addTarefaLayout.INVISIBLE);
                }
            }
        });
        //Localiza a janela Add tarefa
        addTarefaLayout = findViewById(R.id.addTarefaLayout);

        menuBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    efeitoOpenAndClose(menuBotao,params);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(addTarefaLayout.getVisibility() == addTarefaLayout.VISIBLE){
                    addTarefaLayout.setVisibility(addTarefaLayout.INVISIBLE);
                }else{
                    addTarefaLayout.setVisibility(addTarefaLayout.VISIBLE);
                }

            }
        });
        //localizar na activity.
        botaoAddTarefa = findViewById(R.id.botaoAddTarefa_id);
        addNomeTarefa = findViewById(R.id.nomeAddTarefa_id);
        addDescricaoTarefa = findViewById(R.id.textoAddDesc_id);
        viewTarefas = findViewById(R.id.listaTarefasDo);
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
        botaoAddTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (addNomeTarefa.getText().toString().equals("") || addDescricaoTarefa.getText().toString().equals("") ) {
                        Toast.makeText(getApplicationContext(), "Existem campos não preenchidos", Toast.LENGTH_SHORT).show();
                    } else {
                        itemTarefa = new Tarefa();
                        criarTarefa(itemTarefa);
                        addTarefaLayout.setVisibility(addTarefaLayout.INVISIBLE);
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
        tarefa.setNome(addNomeTarefa.getText().toString());
        tarefa.setDescricao(addDescricaoTarefa.getText().toString());
        bancoDadosTarefas.execSQL("INSERT INTO tarefas (nome, descricao) VALUES ('" + tarefa.getNome() + "','" + tarefa.getDescricao() + "')");
        addNomeTarefa.setText("");
    }

    private void removerTarefa(Integer id) {
        try {
            bancoDadosTarefas.execSQL("DELETE FROM tarefas WHERE id=" + id.toString());
            recuperarListaDoIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void efeitoOpenAndClose(LinearLayout view, final ViewGroup.LayoutParams params){
        if(flag){
            TransitionManager.beginDelayedTransition(view);
                    params.width = dpToPx(349);
                    view.setLayoutParams(params);
       }else{
            TransitionManager.beginDelayedTransition(view);
            params.width = dpToPx(55);
            view.setLayoutParams(params);
        }
       flag = !flag;
    }
    //converte dp para px
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    //converte px para dp
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
