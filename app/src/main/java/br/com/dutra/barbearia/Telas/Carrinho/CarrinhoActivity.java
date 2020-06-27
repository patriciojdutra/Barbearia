package br.com.dutra.barbearia.Telas.Carrinho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeProdutosNoCarrinho;
import br.com.dutra.barbearia.Modelo.Carrinho;
import br.com.dutra.barbearia.Telas.Produto.ListaDeProdutosActivity;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.DinheiroUtils;
import br.com.dutra.barbearia.Utilidades.TrocarTela;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import br.com.dutra.barbearia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private Activity act = this;
    private ListView listViewCarrinho;
    private Button btnComprar;
    private TextView txtTotalDeTodosProdutoCarrinho;
    private CardView cardViewCarrinho;

    List<Carrinho> listaDeProdutoNoCarrinho = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        associacaoDosComponentes();
        buscarMeusProdutosNoCarrinho();

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrocarTela.irParaCadastroDePagamento(act);
            }
        });

    }

    public void associacaoDosComponentes(){
        listViewCarrinho = findViewById(R.id.listViewCarrinho);
        btnComprar = findViewById(R.id.btnComprar);
        txtTotalDeTodosProdutoCarrinho = findViewById(R.id.txtTotalDeTodosProdutoCarrinho);
        cardViewCarrinho = findViewById(R.id.cardViewCarrinho);
        cardViewCarrinho.setVisibility(View.GONE);
    }

    public void buscarMeusProdutosNoCarrinho(){

        String uid = FirebaseAuth.getInstance().getUid();

        AlertaUtils.dialogLoad(act);
        FirebaseFirestore.getInstance().collection("Carrinho")
                .whereEqualTo("uidDousuario", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        listaDeProdutoNoCarrinho.clear();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {
                                Carrinho carrinho = doc.toObject(Carrinho.class);
                                carrinho.setId(doc.getId());
                                listaDeProdutoNoCarrinho.add(carrinho);
                            }
                        }

                        AlertaUtils.getDialog().dismiss();
                        calcularTotalDeTodosOsProdutos();
                        carregarLista();
                    }
                });

    }

    public void calcularTotalDeTodosOsProdutos(){
        double dValorTotal = 0;

        for(int i = 0; i<listaDeProdutoNoCarrinho.size(); i++){
            double dValorSubTotal = listaDeProdutoNoCarrinho.get(i).getPrecoDoProduto() * listaDeProdutoNoCarrinho.get(i).getQuantidadeDeProduto();
            dValorTotal = dValorTotal + dValorSubTotal;
        }

        if(listaDeProdutoNoCarrinho.isEmpty()){
            cardViewCarrinho.setVisibility(View.GONE);
            dialogDeListaVazia();
        }else {
            cardViewCarrinho.setVisibility(View.VISIBLE);
            txtTotalDeTodosProdutoCarrinho.setText("Total: "+DinheiroUtils.convertDouble(dValorTotal));
        }
    }

    public void carregarLista(){

        AdaptadorListaDeProdutosNoCarrinho adpter = new AdaptadorListaDeProdutosNoCarrinho(listaDeProdutoNoCarrinho,CarrinhoActivity.this,act);
        listViewCarrinho.setAdapter(adpter);

        listViewCarrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Carrinho carrinho = listaDeProdutoNoCarrinho.get(i);

//                Produto produto = new Produto();
//
//                Intent it = new Intent(act, DescricaoProdutoActivity.class);
//                it.putExtra("produto",produto);
//                startActivity(it);

            }
        });
    }

    public void removerProdutoDocarrinho(Carrinho carrinho){

        AlertaUtils.dialogLoad(act);
        FirebaseFirestore.getInstance().collection("Carrinho")
                .document(carrinho.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        AlertaUtils.getDialog().dismiss();
                        buscarMeusProdutosNoCarrinho();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogSimples(e.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }, act);
                    }
                });

    }

    public void dialogDeConfirmacaoDeRemocaoDeProduto(final Carrinho carrinho) {

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Alerta");
        builder.setIcon(R.drawable.logo250);
        builder.setMessage("Você realmente deseja remover esse produto do carrinho?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                removerProdutoDocarrinho(carrinho);

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public void dialogDeListaVazia(){

        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle("Aviso");
        builder.setIcon(R.drawable.logo250);
        builder.setMessage("Não há produtos em seu carrinho ainda, deseja adicionar?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

                Intent mudarTela = new Intent(getApplicationContext(), ListaDeProdutosActivity.class);
                startActivity(mudarTela);
                finish();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int res) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }


}
