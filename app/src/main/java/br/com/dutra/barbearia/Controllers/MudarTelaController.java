package br.com.dutra.barbearia.Controllers;

import android.app.Activity;
import android.content.Intent;

import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;
import br.com.dutra.barbearia.Telas.Usuario.CadastroUsuarioActivity;


public class MudarTelaController {

    public static void irParaDashboard(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, DashBoardActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaCadastroDeUsuario(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, CadastroUsuarioActivity.class));
        if(finalizarTela)
            act.finish();
    }

}
