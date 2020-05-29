package br.com.dutra.barbearia.Modelo.Usuario;

public class Cartao {

    private String idDoUsuario;
    private String tipoCartao = "CREDIT_CARD";
    private String mesValido;
    private String anoValido;
    private String numeroDoCartao;
    private String codigoDocartão;
    private String NomeNoCartão;
    private String dataDeNascimento;
    private String CpfOuCnpj;

    public Cartao() {

    }

    public Cartao(String idDoUsuario, String mesValido, String anoValido, String numeroDoCartao, String codigoDocartão, String nomeNoCartão, String dataDeNascimento, String cpfOuCnpj) {
        this.idDoUsuario = idDoUsuario;
        this.mesValido = mesValido;
        this.anoValido = anoValido;
        this.numeroDoCartao = numeroDoCartao;
        this.codigoDocartão = codigoDocartão;
        this.NomeNoCartão = nomeNoCartão;
        this.dataDeNascimento = dataDeNascimento;
        this.CpfOuCnpj = cpfOuCnpj;
    }

    public String getIdDoUsuario() {
        return idDoUsuario;
    }

    public void setIdDoUsuario(String idDoUsuario) {
        this.idDoUsuario = idDoUsuario;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }

    public String getMesValido() {
        return mesValido;
    }

    public void setMesValido(String mesValido) {
        this.mesValido = mesValido;
    }

    public String getAnoValido() {
        return anoValido;
    }

    public void setAnoValido(String anoValido) {
        this.anoValido = anoValido;
    }

    public String getNumeroDoCartao() {
        return numeroDoCartao;
    }

    public void setNumeroDoCartao(String numeroDoCartao) {
        this.numeroDoCartao = numeroDoCartao;
    }

    public String getCodigoDocartão() {
        return codigoDocartão;
    }

    public void setCodigoDocartão(String codigoDocartão) {
        this.codigoDocartão = codigoDocartão;
    }

    public String getNomeNoCartão() {
        return NomeNoCartão;
    }

    public void setNomeNoCartão(String nomeNoCartão) {
        NomeNoCartão = nomeNoCartão;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpfOuCnpj() {
        return CpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        CpfOuCnpj = cpfOuCnpj;
    }
}
