package br.com.dutra.barbearia.Telas.Produto;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import br.com.dutra.barbearia.Modelo.Carrinho;
import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import br.com.dutra.barbearia.Utilidades.DinheiroUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

public class DescricaoProdutoActivity extends AppCompatActivity {

    private Activity act = this;

    private ImageView imgProdutoDescricao;
    private TextView txtNomeDescricao;
    private TextView txtPrecoDescricao;
    private TextView txtQuantDescricao;
    private TextView txtDescricao;
    private EditText txtQuantidadeSelecionada;
    private TextView btnMais;
    private TextView btnMenos;
    private TextView btnAdicionarNaSacola;

    private int quantidadeDeItem = 1;
    Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao_produto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgProdutoDescricao = findViewById(R.id.imgProdutoDescricao);
        txtNomeDescricao = findViewById(R.id.txtNomeDescricao);
        txtPrecoDescricao = findViewById(R.id.txtPrecoDescricao);
        txtQuantDescricao = findViewById(R.id.txtQuantDescricao);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtQuantidadeSelecionada = findViewById(R.id.txtQuantidadeSelecionada);
        btnAdicionarNaSacola = findViewById(R.id.btnAdicionarNaSacola);
        btnMais = findViewById(R.id.btnMais);
        btnMenos = findViewById(R.id.btnMenos);
        txtQuantidadeSelecionada.setText(String.valueOf(quantidadeDeItem));

        getProduto();
        ClicksDosBotoes();

    }

    private void ClicksDosBotoes() {

        btnAdicionarNaSacola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionaProdutoNaSacola();
            }
        });

        btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantidadeDeItem++;
                txtQuantidadeSelecionada.setText(String.valueOf(quantidadeDeItem));
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantidadeDeItem--;
                txtQuantidadeSelecionada.setText(String.valueOf(quantidadeDeItem));
            }
        });
    }

    public void getProduto(){

        Bundle extras = getIntent().getExtras();
        produto = (Produto) extras.get("produto");

        txtNomeDescricao.setText(produto.getNome());
        txtPrecoDescricao.setText(DinheiroUtils.convertDouble(produto.getPreco()));
        txtQuantDescricao.setText("Estoque: " + produto.getQuantidade());
        txtDescricao.setText(produto.getDescricao());

        if(produto.getDescricao().isEmpty()){
            txtDescricao.setText("\n\n\nProduto sem descrição!");
            txtDescricao.setTextColor(Color.LTGRAY);
            txtDescricao.setTextSize(24);
        }

        String nomeDoArquivo = produto.getNome();
        File file = getApplicationContext().getFileStreamPath(nomeDoArquivo);

        if(file.exists()){
            Uri uri = Uri.fromFile(file);
            imgProdutoDescricao.setImageURI(uri);
        }
    }

    public void adicionaProdutoNaSacola(){

        String uid = FirebaseAuth.getInstance().getUid();
        String nome = produto.getNome();
        double preco = produto.getPreco();
        int quantidade = Integer.parseInt(txtQuantidadeSelecionada.getText().toString());
        long data = new Date().getTime();

        Carrinho carrinho = new Carrinho(uid,nome,preco,quantidade,data);

        AlertaUtils.dialogLoad(act);
        FirebaseFirestore.getInstance().collection("Carrinho")
                .add(carrinho)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogProdutoAdicionadoaoCarrinhoComSucesso(act);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        AlertaUtils.getDialog().dismiss();
                        AlertaUtils.dialogProdutoNãoAdicionadoAoCarrinho(e.getMessage(),act);

                    }
                });

    }
}
