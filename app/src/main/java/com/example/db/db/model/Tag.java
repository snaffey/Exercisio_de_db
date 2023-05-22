package com.example.db.db.model;

public class Tag {
    int id;
    String nome;

    //construtores
    public Tag(){}

    public Tag(String nome){
        this.nome = nome;
    }

    //setter e getter
    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

}