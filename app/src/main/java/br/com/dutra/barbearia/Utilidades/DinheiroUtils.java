package br.com.dutra.barbearia.Utilidades;

import java.text.NumberFormat;

public class DinheiroUtils {

    static NumberFormat moeda = NumberFormat.getCurrencyInstance();

    public static String convertDouble(double dValor){
        String dinheiroformatado = moeda.format(dValor);
        return dinheiroformatado;
    }

    public static String convertString(String sValor){

        sValor = sValor.replace(".",",");
        double dValor = Double.parseDouble(sValor);
        String dinheiroformatado = moeda.format(dValor);
        return dinheiroformatado;

    }

    public static double converteStrigEmDouble(String sValor){

        try {
            sValor = sValor.replace(".", "");
            sValor = sValor.replace(",", ".");
            double dValor = Double.parseDouble(sValor);
            return dValor;
        }catch (Exception e){
            return 0;
        }

    }

}
