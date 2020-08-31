package br.com.dutra.barbearia.Modelo;

public class Banner {

    private String descricao = "";
    private String corDoTexto = "branco";
    private int tamanhoDoTexto = 12;
    private String url = "";

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCorDoTexto() {
        return corDoTexto;
    }

    public void setCorDoTexto(String corDoTexto) {
        this.corDoTexto = corDoTexto;
    }

    public int getTamanhoDoTexto() {
        return tamanhoDoTexto;
    }

    public void setTamanhoDoTexto(int tamanhoDoTexto) {
        this.tamanhoDoTexto = tamanhoDoTexto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
