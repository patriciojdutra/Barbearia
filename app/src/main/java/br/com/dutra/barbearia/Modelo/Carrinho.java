package br.com.dutra.barbearia.Modelo;

public class Carrinho {

    private String id;
    private String uidDousuario;
    private String nomeDoProduto;
    private double precoDoProduto;
    private int quantidadeDeProduto;
    private long dataDeCriacaodoCarrinho;

    public Carrinho(String uidDousuario, String nomeDoProduto, double precoDoProduto, int quantidadeDeProduto, long dataDeCriacaodoCarrinho) {
        this.uidDousuario = uidDousuario;
        this.nomeDoProduto = nomeDoProduto;
        this.precoDoProduto = precoDoProduto;
        this.quantidadeDeProduto = quantidadeDeProduto;
        this.dataDeCriacaodoCarrinho = dataDeCriacaodoCarrinho;
    }

    public Carrinho() {
    }

    public String getUidDousuario() {
        return uidDousuario;
    }

    public void setUidDousuario(String uidDousuario) {
        this.uidDousuario = uidDousuario;
    }

    public String getNomeDoProduto() {
        return nomeDoProduto;
    }

    public void setNomeDoProduto(String nomeDoProduto) {
        this.nomeDoProduto = nomeDoProduto;
    }

    public double getPrecoDoProduto() {
        return precoDoProduto;
    }

    public void setPrecoDoProduto(double precoDoProduto) {
        this.precoDoProduto = precoDoProduto;
    }

    public int getQuantidadeDeProduto() {
        return quantidadeDeProduto;
    }

    public void setQuantidadeDeProduto(int quantidadeDeProduto) {
        this.quantidadeDeProduto = quantidadeDeProduto;
    }

    public long getDataDeCriacaodoCarrinho() {
        return dataDeCriacaodoCarrinho;
    }

    public void setDataDeCriacaodoCarrinho(long dataDeCriacaodoCarrinho) {
        this.dataDeCriacaodoCarrinho = dataDeCriacaodoCarrinho;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
