package br.com.dutra.barbearia.Modelo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Atendimento {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String data;
    private String horario;
    private String status;
    private String cliente;
    private String tipoDeCorte;
    private String nomeBarbeiro;
    private String valor;
    private boolean emDomicilio;

    public Atendimento(String data, String horario, String status, String cliente, String tipoDeCorte, String nomeBarbeiro,String valor, boolean emDomicilio) {
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.cliente = cliente;
        this.tipoDeCorte = tipoDeCorte;
        this.nomeBarbeiro = nomeBarbeiro;
        this.valor = valor;
        this.emDomicilio = emDomicilio;
    }

    public Atendimento() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipoDeCorte() {
        return tipoDeCorte;
    }

    public void setTipoDeCorte(String tipoDeCorte) {
        this.tipoDeCorte = tipoDeCorte;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }

    public void setNomeBarbeiro(String nomeBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isEmDomicilio() {
        return emDomicilio;
    }

    public void setEmDomicilio(boolean emDomicilio) {
        this.emDomicilio = emDomicilio;
    }
}
