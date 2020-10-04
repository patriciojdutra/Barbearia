package br.com.dutra.barbearia.Modelo;

import java.util.Date;

public class Despesa {

    String idDespesa = "";
    Date data = new Date();
    String despesa = "";
    double preco = 0;
    String observacao = "";

    public String getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(String idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDespesa() {
        return despesa;
    }

    public void setDespesa(String despesa) {
        this.despesa = despesa;
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
