package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.DialogInterface;

import java.util.Calendar;

public class ConvertUtils {


    public static Calendar ConverteStringParaData(String data, Activity act){

        try {
            String sDia = data.substring(0, 2);
            String sMes = data.substring(3, 5);
            String sAno = data.substring(6, 10);

            int iDia = Integer.parseInt(sDia);
            int iMes = Integer.parseInt(sMes);
            int iAno = Integer.parseInt(sAno);

            Calendar calendar = Calendar.getInstance();
            calendar.set(iAno, iMes, iDia);
            return calendar;

        }catch (Exception e){

            AlertaUtils.dialogSimples("Ocorreu um erro com a data selecionada!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }, act);
            return Calendar.getInstance();
        }
    }

    public static String retornaDiaDaSemana(Calendar calendar, Activity act) {

        switch (calendar.DAY_OF_WEEK) {

            case Calendar.MONDAY:
                return "Segunda";
            case Calendar.TUESDAY:
                return "Terca";
            case Calendar.WEDNESDAY:
                return "Quarta";
            case Calendar.THURSDAY:
                return "Quinta";
            case Calendar.FRIDAY:
                return "Sexta";
            case Calendar.SATURDAY:
                return "Sabado";
            case Calendar.SUNDAY:
                return "Domingo";
            default:
                AlertaUtils.dialogSimples("Ocorreu um erro ao buscar o dia da semana", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }, act);
                return "";
        }

    }



}
