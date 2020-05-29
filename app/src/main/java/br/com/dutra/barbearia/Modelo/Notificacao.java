package br.com.dutra.barbearia.Modelo;

public class Notificacao {

    private String fromName;
    private String fromId;
    private String toId;
    private String text;
    private long setTimestamp;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSetTimestamp() {
        return setTimestamp;
    }

    public void setSetTimestamp(long setTimestamp) {
        this.setTimestamp = setTimestamp;
    }

}
