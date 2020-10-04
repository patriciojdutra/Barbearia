package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;


import br.com.dutra.barbearia.Telas.Sistema.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilitario {

    public static String Url = "https://sandbox.moip.com.br/v2/";

    public static boolean verifiacrAutencicao(Activity act) {

        try {

            String uid = FirebaseAuth.getInstance().getUid();

            if (uid==null) {
                return false;
            }
            else {
                return true;
            }

        }catch (Exception e){ }

        return false;
    }

    public static void updateToken() {
        String token = FirebaseAuth.getInstance().getUid();
        String uid = FirebaseAuth.getInstance().getUid();

        if (uid != null) {
            FirebaseFirestore.getInstance().collection("Usuario")
                    .document(uid)
                    .update("token", token);
        }
    }

    public static void setDataTextView(TextView textView, Activity act) {

        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(act, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String dia = String.valueOf(dayOfMonth);
                String mes = String.valueOf(monthOfYear+1);
                String ano = String.valueOf(year);

                if((monthOfYear + 1) < 10)
                {
                    mes = (0+mes);
                }
                if(dayOfMonth<10)
                {
                    dia = (0+dia);
                }

                textView.setText(dia + "/" + mes + "/" + ano);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public static void setDataTextView(TextView textView, Activity act, DatePickerDialog datePickerDialog) {
        datePickerDialog.show();
    }

    public static String dataEHoraAtual(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        String data = formataData.format(new Date());
        return data.replace("-","/");
    }

    public static String dataAtual(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        String data = formataData.format(new Date());
        return data.replace("-","/");
    }

    public static String getMesAtual(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        String data = formataData.format(new Date());
        String mes = data.substring(3,5);

        if(mes == "01"){
            return "janeiro";
        }else if(mes.equals("02")){
            return "Fevereiro";
        }else if(mes.equals("03")){
            return "Março";
        }else if(mes.equals("04")){
            return "Abril";
        }else if(mes.equals("05")){
            return "Maio";
        }else if(mes.equals("06")){
            return "Junho";
        }else if(mes.equals("07")){
            return "Julho";
        }else if(mes.equals("08")){
            return "Agosto";
        }else if(mes.equals("09")){
            return "Setembro";
        }else if(mes.equals("10")){
            return "Outubro";
        }else if(mes.equals("11")){
            return "Novembro";
        }else {
            return "Dezembro";
        }
    }

    public static String getMesAtualNumero(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        String data = formataData.format(new Date());
        String mes = data.substring(3,5);

        return mes;

//        if(mes == "01"){
//            return 1;
//        }else if(mes.equals("02")){
//            return 2;
//        }else if(mes.equals("03")){
//            return 3;
//        }else if(mes.equals("04")){
//            return 4;
//        }else if(mes.equals("05")){
//            return 5;
//        }else if(mes.equals("06")){
//            return 6;
//        }else if(mes.equals("07")){
//            return 7;
//        }else if(mes.equals("08")){
//            return 8;
//        }else if(mes.equals("09")){
//            return 9;
//        }else if(mes.equals("10")){
//            return 10;
//        }else if(mes.equals("11")){
//            return 11;
//        }else {
//            return 12;
//        }
    }


    public static String getDataInicio(){
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formataData.format(new Date());
        String dataInicio = "01/" + data.substring(3,10);

        return dataInicio;
    }

    public static String getDataFinal( String sData){

        Date dData = converteStringEmData(sData);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dData);
        cal.add(Calendar.MONTH, 1);
        dData = cal.getTime();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formato.format(dData);

        return "01/"+dataFormatada.substring(3,10);
    }

    public static String horaAtual(){
        SimpleDateFormat formataData = new SimpleDateFormat("HH:mm:ss");
        String hora = formataData.format(new Date());
        return hora;
    }

    public static Date converteStringEmData(String dataRecebida) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date dataFormatada = formato.parse(dataRecebida);
            return dataFormatada;
        }catch (Exception e){
            return  null;
        }
    }

}
