package br.com.dutra.barbearia.Modelo.Usuario;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String uiId = "";
    private String idWireCard = "";
    private String nome = "";
    private String sobrenome = "";
    private String celular = "";
    private String dataDeNascimento = "";
    private String cpfouCnpj;
    private String senha = "";
    private String userLogin = "";
    private String tipoUsuario = "C";
    private String token;

    public Usuario() { }

    public Usuario(String uiId, String nome, String celular, String dataDeNascimento, String senha, String userLogin) {
        this.uiId = uiId;
        this.nome = nome;
        this.celular = celular;
        this.dataDeNascimento = dataDeNascimento;
        this.senha = senha;
        this.userLogin = userLogin;
    }

    public String getUiId() {
        return uiId;
    }

    public void setUiId(String uiId) {
        this.uiId = uiId;
    }

    public String getIdWireCard() {
        return idWireCard;
    }

    public void setIdWireCard(String idWireCard) {
        this.idWireCard = idWireCard;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpfouCnpj() {
        return cpfouCnpj;
    }

    public void setCpfouCnpj(String cpfouCnpj) {
        this.cpfouCnpj = cpfouCnpj;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
