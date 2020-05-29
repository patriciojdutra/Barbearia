package br.com.dutra.barbearia.controller;

import android.app.Activity;
import android.content.Intent;

import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;


public class MudarTelaController {

    public static void irParaDashboard(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, DashBoardActivity.class));
        if(finalizarTela)
            act.finish();
    }

}
