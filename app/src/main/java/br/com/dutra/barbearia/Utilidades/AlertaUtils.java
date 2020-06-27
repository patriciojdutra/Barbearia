package br.com.dutra.barbearia.Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ProgressBar;

import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.Carrinho.CarrinhoActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AlertaUtils {

    private static AlertDialog.Builder alert;
    private static Dialog dialog;

    public static AlertDialog.Builder getAlert() {
        return alert;
    }

    public static Dialog getDialog() {
        return dialog;
    }

    public static void dialogSimples(String txt, DialogInterface.OnClickListener clickListener, Activity act) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.logo250);
        builder.setMessage(txt);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", clickListener);

        //cria o AlertDialog
        alerta = builder.create();
        alerta.show();
    }

    public static void dialogLoad(Activity act) {

        alert = new AlertDialog.Builder(act);
        alert.setTitle("Aguarde um momento...");
        alert.setIcon(R.drawable.logo250);
        alert.setMessage("Carregando");
        alert.setCancelable(false);
        final ProgressBar input = new ProgressBar (act);
        alert.setView(input);

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface aleDialog, int whichButton) {

            }
        });
        dialog = alert.create();
        dialog.show();
    }

    public static void dialogConfirmacaoLogout(final Activity act) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.logo250);
        builder.setMessage("Você realmente deseja sair de sua conta?");

        builder.setPositiveButton("NÂO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        builder.setNegativeButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {
                FirebaseAuth.getInstance().signOut();
                MudarTelaController.irParaTelaDeLogin(true, act);
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public static void dialogProdutoAdicionadoaoCarrinhoComSucesso(final Activity act) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Sucesso");
        builder.setIcon(R.drawable.baseline_access_time_24);
        builder.setMessage("Produto Adicionado ao carrinho com sucesso, deseja ir para o carrinho?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {
                TrocarTela.irParaCarrinho(act);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {
               act.finish();
            }
        });

        alerta = builder.create();
        alerta.show();

    }

    public static void dialogProdutoNãoAdicionadoAoCarrinho(String erro, final Activity act) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Houve um erro");
        builder.setIcon(R.drawable.baseline_access_time_24);
        builder.setMessage(erro);
        alerta = builder.create();
        alerta.show();
    }

    public static void dialogDadosDePagamentoOk(final Activity act) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.logo250);
        builder.setMessage("Você deseja alterar dados do cartão, ou endereço de entrega?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                Intent it = new Intent(act.getApplicationContext(), CarrinhoActivity.class);
                it.putExtra("dadosCadastrados",true);
                act.startActivity(it);
                act.finish();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

}
