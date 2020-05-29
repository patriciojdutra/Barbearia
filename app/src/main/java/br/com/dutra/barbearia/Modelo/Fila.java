package br.com.dutra.barbearia.Modelo;

public class Fila {

    private int clientesEsperando = 0;
    private String tempoDeEspera = "";

    public int getClientesEsperando() {
        return clientesEsperando;
    }

    public void setClientesEsperando(int clientesEsperando) {
        this.clientesEsperando = clientesEsperando;
    }

    public String getTempoDeEspera() {
        return tempoDeEspera;
    }

    public void setTempoDeEspera(String tempoDeEspera) {
        this.tempoDeEspera = tempoDeEspera;
    }

}
