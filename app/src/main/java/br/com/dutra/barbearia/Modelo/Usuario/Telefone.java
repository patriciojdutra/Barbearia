package br.com.dutra.barbearia.Modelo.Usuario;

public class Telefone {

    private String idDoUsuario;
    private String codigoInternacional;
    private String codigoDeArea;
    private String numeroDoTelefone;

    public Telefone(String idDoUsuario, String codigoInternacional, String codigoDeArea, String numeroDoTelefone) {
        this.idDoUsuario = idDoUsuario;
        this.codigoInternacional = codigoInternacional;
        this.codigoDeArea = codigoDeArea;
        this.numeroDoTelefone = numeroDoTelefone;
    }

    public String getIdDoUsuario() {
        return idDoUsuario;
    }

    public void setIdDoUsuario(String idDoUsuario) {
        this.idDoUsuario = idDoUsuario;
    }

    public String getCodigoInternacional() {
        return codigoInternacional;
    }

    public void setCodigoInternacional(String codigoInternacional) {
        this.codigoInternacional = codigoInternacional;
    }

    public String getCodigoDeArea() {
        return codigoDeArea;
    }

    public void setCodigoDeArea(String codigoDeArea) {
        this.codigoDeArea = codigoDeArea;
    }

    public String getNumeroDoTelefone() {
        return numeroDoTelefone;
    }

    public void setNumeroDoTelefone(String numeroDoTelefone) {
        this.numeroDoTelefone = numeroDoTelefone;
    }
}
