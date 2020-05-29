package br.com.dutra.barbearia.Modelo;

import java.io.Serializable;

public class Produto implements Serializable {

    private String nome = "";
    private double preco;
    private String tipo;
    private int quantidade;
    private String descricao = "";

    public Produto(String nome, double preco, String tipo, int quantidade, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public Produto() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
