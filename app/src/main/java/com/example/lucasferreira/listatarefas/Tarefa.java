package com.example.lucasferreira.listatarefas;


public class Tarefa {
    private String nome;
    private int status = 0;
    private String descricao;
    private static int id = 0;

    public Tarefa(){
        id = id++;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public int getId(){
        return id;
    }
}
