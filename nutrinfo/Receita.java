package com.app.nutrinfo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Receita {
    private String receitaId;
    private String titulo;
    private String preparo;
    private String categoria;
    private List<Ingrediente> ingredientes;

    public Receita(){
    }

    public Receita(String receitaId, String titulo, String preparo, String categoria, List<Ingrediente> ingredientes) {
        this.receitaId = receitaId;
        this.titulo = titulo;
        this.preparo = preparo;
        this.categoria = categoria;
        this.ingredientes = ingredientes;
    }

    public String getReceitaId() { return receitaId; }

    public String getTitulo() {
        return titulo;
    }

    public String getPreparo() {
        return preparo;
    }

    public String getCategoria() { return categoria; }

    public List<Ingrediente> getIngredientes() { return ingredientes; }
}
