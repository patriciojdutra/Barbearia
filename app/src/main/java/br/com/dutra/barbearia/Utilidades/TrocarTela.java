package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.content.Intent;

import br.com.dutra.barbearia.Telas.BatePapo.ListaDeConversasActivity;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import br.com.dutra.barbearia.Telas.Corte.ListaDeCortesActivity;
import br.com.dutra.barbearia.Telas.Pagamento.CadastroDePagamentoActivity;
import br.com.dutra.barbearia.Telas.Produto.ListaDeProdutosActivity;
import br.com.dutra.barbearia.Telas.Sistema.DashBoardActivity;

public class TrocarTela {


    public static void irParaDashboar(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), DashBoardActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaListaDeProdutos(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), ListaDeProdutosActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaCarrinho(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), CarrinhoActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaBatePapo(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), ListaDeConversasActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaLocalizacao(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), DashBoardActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaListaCortes(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), ListaDeCortesActivity.class);
        act.startActivity(it);
        act.finish();;
    }

    public static void irParaCadastroDePagamento(Activity act) {
        Intent it = new Intent(act.getApplicationContext(), CadastroDePagamentoActivity.class);
        act.startActivity(it);
        act.finish();;
    }


}
