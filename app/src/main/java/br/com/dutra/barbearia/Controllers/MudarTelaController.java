package br.com.dutra.barbearia.Controllers;

import android.app.Activity;
import android.content.Intent;

import br.com.dutra.barbearia.Telas.Agendamento.AgendaActivity;
import br.com.dutra.barbearia.Telas.Atividade.AtividadeActivity;
import br.com.dutra.barbearia.Telas.Local.MapsActivity;
import br.com.dutra.barbearia.Telas.Produto.ListaDeProdutosActivity;
import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;
import br.com.dutra.barbearia.Telas.Sistema.LoginActivity;
import br.com.dutra.barbearia.Telas.Sistema.ServicosActivity;
import br.com.dutra.barbearia.Telas.Usuario.CadastroUsuarioActivity;


public class MudarTelaController {

    public static void irParaDashboard(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, DashBoardActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaTelaDeLogin(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, LoginActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaCadastroDeUsuario(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, CadastroUsuarioActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaAtividade(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, AtividadeActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaLoja(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, ListaDeProdutosActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaServicos(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, ServicosActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaAgendamento(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, AgendaActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaEnderecos(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, MapsActivity.class));
        if(finalizarTela)
            act.finish();
    }

    public static void irParaContatos(boolean finalizarTela, Activity act){
        act.startActivity(new Intent(act, ServicosActivity.class));
        if(finalizarTela)
            act.finish();
    }

}
