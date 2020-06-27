package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import br.com.dutra.barbearia.R;

public class MyDialog  {

    public static final String erroAddrPn = "Não foi possível add por isso e por aquilo";

    public static void msg(Activity act, DialogInterface.OnClickListener eventConfirmacao, DialogInterface.OnClickListener eventNegacao, String mesg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("Aviso");
        alertDialog.setMessage(mesg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok", eventConfirmacao);
        alertDialog.setNegativeButton("cancel", eventNegacao);
        alertDialog.create().show();

    }
}
