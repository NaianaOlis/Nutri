package com.app.nutrinfo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Ingrediente {
    private String ingrId;
    private String medida;
    private String nome;
    private String quantidade;

    public Ingrediente(){
    }

    public Ingrediente( String ingrId, String medida, String nome, String quantidade) {
        this.ingrId = ingrId;
        this.medida = medida;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public String getIngrId() { return ingrId; }

    public String getMedida() {
        return medida;
    }

    public String getNome() { return nome; }

    public String getQuantidade() {
        return quantidade;
    }

    public void setIngrId(String ingrId) {
        this.ingrId = ingrId;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
