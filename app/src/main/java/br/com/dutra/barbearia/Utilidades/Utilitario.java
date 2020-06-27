package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import br.com.dutra.barbearia.Telas.Sistema.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

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

                if(monthOfYear<10)
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

}
