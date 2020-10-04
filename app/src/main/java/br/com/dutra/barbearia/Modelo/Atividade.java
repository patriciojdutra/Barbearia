package br.com.dutra.barbearia.Modelo;

import java.util.Date;

public class Atividade {

    String idAtividade = "";
    Date data = new Date();
    String nome = "";
    String telefone = "";
    String comoNosConheceu = "";
    String tipoAtividade = "";
    String item = "";
    String idItem = "";
    double preco = 0;
    String observacao = "";

    public String getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(String idAtividade) {
        this.idAtividade = idAtividade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getComoNosConheceu() {
        return comoNosConheceu;
    }

    public void setComoNosConheceu(String comoNosConheceu) {
        this.comoNosConheceu = comoNosConheceu;
    }

    public String getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(String tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
