package br.com.dutra.barbearia.Modelo.Usuario;

public class Endereco {

    private String uidUsuario = "";
    private String cidade = "";
    private String complemento = "";
    private String bairro = "";
    private String rua = "";
    private String numero = "";
    private String cep = "";
    private String estado;
    private String pais = "BR";

    public Endereco() { }

    public Endereco(String uid, String cidade, String complemento, String bairro, String rua, String numero, String cep, String estado) {
        this.uidUsuario = uid;
        this.cidade = cidade;
        this.complemento = complemento;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.estado = estado;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
