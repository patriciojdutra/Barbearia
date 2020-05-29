package br.com.dutra.barbearia.Modelo;

import java.util.ArrayList;
import java.util.List;

public class Horario {

    List<String> HorariosDeAtendimento = new ArrayList<>();

    public List<String> getHorariosDeAtendimento() {
        return HorariosDeAtendimento;
    }

    public void setHorariosDeAtendimento(List<String> horariosDeAtendimento) {
        HorariosDeAtendimento = horariosDeAtendimento;
    }
}
