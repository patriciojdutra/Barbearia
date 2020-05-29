package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.Intent;


import br.com.dutra.barbearia.Telas.Sistema.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utilitario {

    public static String Url = "https://sandbox.moip.com.br/v2/";

    public static boolean verifiacrAutencicao(Activity act) {

        try {

            String uid = FirebaseAuth.getInstance().getUid();

            if (uid==null) {
                Intent intent = new Intent(act, LoginActivity.class);
                act.startActivity(intent);
                act.finish();
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


}
