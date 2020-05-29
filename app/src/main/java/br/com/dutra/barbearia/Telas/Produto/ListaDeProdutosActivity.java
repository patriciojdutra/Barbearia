package br.com.dutra.barbearia.Telas.Produto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeProdutos;
import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaDeProdutosActivity extends AppCompatActivity {

    ListView listView;
    private List<Produto> listaDeProduto = new ArrayList<>();
    private Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_produtos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        associacao();
        baixarDados();

    }

    public void associacao(){

        listView = findViewById(R.id.listView);

    }

    public void baixarDados(){

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Produto").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                        if (!docs.isEmpty()) {

                            for (DocumentSnapshot doc : docs) {
                                Produto produto = doc.toObject(Produto.class);
                                listaDeProduto.add(produto);
                            }
                        }

                        AlertaUtils.getDialog().dismiss();
                        carregarLista();
                    }
                });

    }

    public void carregarLista(){

        AdaptadorListaDeProdutos adpter = new AdaptadorListaDeProdutos(listaDeProduto,act);
        listView.setAdapter(adpter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Produto produto = listaDeProduto.get(i);

                Intent it = new Intent(act, DescricaoProdutoActivity.class);
                it.putExtra("produto",produto);
                startActivity(it);

            }
        });
    }

}
