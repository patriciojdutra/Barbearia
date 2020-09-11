package br.com.dutra.barbearia.Telas.Produto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import br.com.dutra.barbearia.Adaptadores.AdaptadorListaDeProdutos;
import br.com.dutra.barbearia.Controllers.MudarTelaController;
import br.com.dutra.barbearia.Modelo.Produto;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Utilidades.AlertaUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.action_cabelos);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.action_cabelos) {
                    baixarDados("cabelo");
                }else if(item.getItemId() == R.id.action_roupas) {
                    baixarDados("roupa");
                }else if(item.getItemId() == R.id.action_bolsas) {
                    baixarDados("bolsa");
                }else if(item.getItemId() == R.id.action_brincos) {
                    baixarDados("brinco");
                }else if(item.getItemId() == R.id.action_outros) {
                    baixarDados("outro");
                }

                return true;
            }
        });

        associacao();
        baixarDados("cabelo");

    }

    public void associacao(){

        listView = findViewById(R.id.listView);

    }

    public void baixarDados(String filtro){

        listaDeProduto.clear();

        AlertaUtils.dialogLoad(act);

        FirebaseFirestore.getInstance().collection("Produto")
                .whereEqualTo("tipo",filtro)
                .get()
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

                        AlertaUtils.fecharDialog();
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
