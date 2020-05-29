package br.com.dutra.barbearia.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Conversa {

    private String id;
    private String idGerente;
    private String idCliente;
    List<Mensagem> mensagens = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGerente() {
        return idGerente;
    }

    public void setIdGerente(String idGerente) {
        this.idGerente = idGerente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
}
