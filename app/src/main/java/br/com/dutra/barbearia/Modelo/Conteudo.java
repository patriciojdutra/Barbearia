package br.com.dutra.barbearia.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Conteudo {

    private String titulo = "";
    private String descricao = "";
    private String preco = "";
    private String tipoDeLayout = "";
    private List<String> url = new ArrayList<>();

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getTipoDeLayout() {
        return tipoDeLayout;
    }

    public void setTipoDeLayout(String tipoDeLayout) {
        this.tipoDeLayout = tipoDeLayout;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
