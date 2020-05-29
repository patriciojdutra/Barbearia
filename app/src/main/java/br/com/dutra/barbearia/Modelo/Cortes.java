package br.com.dutra.barbearia.Modelo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Cortes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String nomeDoCorte;
    private String duracaoDoCorte;
    private String idTipoDeCorte;
    private String valorDoCorte;
    private String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeDoCorte() {
        return nomeDoCorte;
    }

    public void setNomeDoCorte(String nomeDoCorte) {
        this.nomeDoCorte = nomeDoCorte;
    }

    public String getDuracaoDoCorte() {
        return duracaoDoCorte;
    }

    public void setDuracaoDoCorte(String duracaoDoCorte) {
        this.duracaoDoCorte = duracaoDoCorte;
    }

    public String getIdTipoDeCorte() {
        return idTipoDeCorte;
    }

    public void setIdTipoDeCorte(String idTipoDeCorte) {
        this.idTipoDeCorte = idTipoDeCorte;
    }

    public String getValorDoCorte() {
        return valorDoCorte;
    }

    public void setValorDoCorte(String valorDoCorte) {
        this.valorDoCorte = valorDoCorte;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
