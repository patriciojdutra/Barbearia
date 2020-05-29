package br.com.dutra.barbearia.Modelo;

public class Mensagem {

    private String msg;
    private long date;
    private String TipoEnvio;

    public Mensagem() {

    }

    public Mensagem(String msg, long date, String tipoEnvio) {
        this.msg = msg;
        this.date = date;
        TipoEnvio = tipoEnvio;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTipoEnvio() {
        return TipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        TipoEnvio = tipoEnvio;
    }
}
